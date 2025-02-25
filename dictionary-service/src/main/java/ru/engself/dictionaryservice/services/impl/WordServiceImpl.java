package ru.engself.dictionaryservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.CommonWordDTO;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.enums.BucketEnum;
import ru.engself.dictionaryservice.mappers.WordMapper;
import ru.engself.dictionaryservice.repositories.WordRepository;
import ru.engself.dictionaryservice.services.CommonWordService;
import ru.engself.dictionaryservice.services.DeckService;
import ru.engself.dictionaryservice.services.WordProgressService;
import ru.engself.dictionaryservice.services.WordService;
import ru.engself.dictionaryservice.utils.feigns.PhotoFeignController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.generateKeyPrefix;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final CommonWordService commonWordService;
    private final DeckService deckService;
    private final WordMapper wordMapper;
    private final PhotoFeignController photoFeignController;
    private final WordProgressService wordProgressService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public WordDTO createWord(UUID deckId, WordDTO wordDTO, Optional<MultipartFile> file, UUID userId) {

        String wordName = wordDTO.getCommonWord().getWordName();
        CommonWordDTO commonWord = commonWordService.getOrCreateCommonWord(wordName, userId);
        DeckDTO deck = deckService.getDeckById(deckId, userId);
        String wordTranslation = wordDTO.getWordTranslation();

        String wordPhoto = file.map(multipartFile -> {
            try {
                return photoFeignController.uploadFile(multipartFile, BucketEnum.WORDS).getFilename();
            } catch (Exception e) {
                return commonWord.getWordPhoto();
            }
        }).orElse(commonWord.getWordPhoto());

        WordDTO word = WordDTO.builder()
                .commonWord(commonWord)
                .deck(deck)
                .wordTranslation(wordTranslation)
                .wordPhoto(wordPhoto)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_period", userId));
        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_deck", userId));
        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_user", userId));
        kafkaTemplate.send("activity-updates", generateKeyPrefix("activity_stats", userId));

        word = wordMapper.toDTO(wordRepository.save(wordMapper.toEntity(word)));
        wordProgressService.createWordProgress(word, userId);

        return word;
    }

    @Override
    public WordDTO updateWordById(UUID wordId, WordDTO wordDTO, Optional<MultipartFile> file, UUID userId) {
        WordDTO currentWord = getWordById(wordId, userId);

        if (wordDTO.getWordTranslation() != null) {
            currentWord.setWordTranslation(wordDTO.getWordTranslation());
        }

        if (file.isPresent()) {
            String newPhoto;
            try {
                newPhoto = photoFeignController.uploadFile(file.get(), BucketEnum.WORDS).getFilename();
            } catch (Exception e) {
                newPhoto = currentWord.getWordPhoto();
            }

            if (!newPhoto.equals(currentWord.getWordPhoto())) {
                String oldPhoto = currentWord.getWordPhoto();
                currentWord.setWordPhoto(newPhoto);

                if (oldPhoto != null && !oldPhoto.isEmpty()) {
                    photoFeignController.deleteFile(oldPhoto, BucketEnum.WORDS);
                }
            }
        }

        currentWord.setUpdatedAt(LocalDateTime.now());
        wordRepository.save(wordMapper.toEntity(currentWord));

        return currentWord;
    }

    @Override
    public WordDTO getWordById(UUID wordId, UUID userId) {
        return wordRepository.findById(wordId)
                .map(wordMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no word with id: " + wordId));
    }

}
