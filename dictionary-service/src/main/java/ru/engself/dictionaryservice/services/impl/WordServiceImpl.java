package ru.engself.dictionaryservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.CommonWordDTO;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.enums.BucketEnum;
import ru.engself.dictionaryservice.exceptions.DeckNotFoundException;
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

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final CommonWordService commonWordService;
    private final DeckService deckService;
    private final WordMapper wordMapper;
    private final PhotoFeignController photoFeignController;
    private final WordProgressService wordProgressService;

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

        word = wordMapper.toDTO(wordRepository.save(wordMapper.toEntity(word)));
        wordProgressService.createWordProgress(word, userId);

        return word;
    }

    @Override
    public WordDTO updateWordById(UUID wordId, WordDTO wordDTO, UUID userId) {
        WordDTO currentWord = getWordById(wordId, userId);

        System.out.println(currentWord);

        String translation = wordDTO.getWordTranslation();

        if (translation != null) {
            currentWord.setWordTranslation(translation);
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
