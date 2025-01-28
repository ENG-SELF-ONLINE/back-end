package ru.engself.dictionaryservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.dictionaryservice.dtos.DeckStatisticsDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.dtos.WordProgressDTO;
import ru.engself.dictionaryservice.entities.WordProgress;
import ru.engself.dictionaryservice.enums.WordReviewResult;
import ru.engself.dictionaryservice.enums.WordStatus;
import ru.engself.dictionaryservice.exceptions.NoWordsForReviewException;
import ru.engself.dictionaryservice.exceptions.WordProgressNotFoundException;
import ru.engself.dictionaryservice.mappers.WordProgressMapper;
import ru.engself.dictionaryservice.repositories.WordProgressRepository;
import ru.engself.dictionaryservice.services.WordProgressService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordProgressServiceImpl implements WordProgressService {

    private final WordProgressRepository wordProgressRepository;
    private final WordProgressMapper wordProgressMapper;

    @Override
    public DeckStatisticsDTO getStatisticsByDeckId(UUID deckId, UUID userId) {
        List<WordProgressDTO> wordProgressDTOs = wordProgressRepository.findAllWordProgressesByDeckId(deckId)
                .stream()
                .map(wordProgressMapper::toDTO)
                .collect(Collectors.toList());
        return calculateStatistics(wordProgressDTOs);
    }

    @Override
    public DeckStatisticsDTO getStatisticsByUserId(UUID userId) {
        List<WordProgressDTO> wordProgressDTOs = wordProgressRepository.findAllWordProgressesByUserId(userId)
                .stream()
                .map(wordProgressMapper::toDTO)
                .collect(Collectors.toList());
        return calculateStatistics(wordProgressDTOs);
    }

    @Override
    public List<WordProgressDTO> getNextWords(UUID deckId, UUID userId) {
        return wordProgressRepository.findNextWordsByDeckId(deckId, LocalDateTime.now()).stream()
                .map(wordProgressMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public void updateWordProgress(UUID wordProgressId, WordReviewResult result, UUID userId) {

        WordProgress wordProgress = wordProgressRepository.findById(wordProgressId)
                .orElseThrow(() -> new WordProgressNotFoundException("Word progress not found with id: " + wordProgressId));

        LocalDateTime nextReviewDate = switch (result) {
            case BAD -> LocalDateTime.now().plusMinutes(5);
            case GOOD -> LocalDateTime.now().plusDays(1);
            case EXCELLENT -> LocalDateTime.now().plusDays(5);
            default -> throw new IllegalArgumentException("Invalid review result");
        };

        wordProgress.setNextReviewDate(nextReviewDate);
        wordProgress.setWordStatus(result == WordReviewResult.EXCELLENT ? WordStatus.REPEATING : WordStatus.LEARNING);

        if (result == WordReviewResult.EXCELLENT && wordProgress.getPreviousResult() == WordReviewResult.EXCELLENT) {
            wordProgressRepository.delete(wordProgress);
        } else {
            wordProgress.setPreviousResult(result);
            wordProgressRepository.save(wordProgress);
        }
    }

    @Override
    public List<WordProgressDTO> getWordsByDeckId(UUID deckId, UUID userId) {
        return wordProgressRepository.findByWordDeckDeckId(deckId).stream()
                .map(wordProgressMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public WordProgressDTO createWordProgress(WordDTO wordDTO, UUID userId) {
        WordProgressDTO wordProgressDTO = WordProgressDTO.builder()
                .word(wordDTO)
                .wordStatus(WordStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return wordProgressMapper.toDTO(
                wordProgressRepository.save(wordProgressMapper.toEntity(wordProgressDTO))
        );
    }

    @Override
    public WordProgressDTO getWordProgressById(UUID wordProgressId, UUID userId) {

        WordProgress wordProgress = wordProgressRepository.findById(wordProgressId).orElseThrow(
                () -> new WordProgressNotFoundException("There is no wordProgress with id: " + wordProgressId)
        );

        return wordProgressMapper.toDTO(wordProgress);
    }

    @Override
    public WordProgressDTO getWordProgressByWordId(UUID wordId, UUID userId) {
        WordProgress wordProgress = wordProgressRepository.findWordProgressByWordWordId(wordId).orElseThrow(
                () -> new WordProgressNotFoundException("There is no wordProgress with id: " + wordId)
        );

        return wordProgressMapper.toDTO(wordProgress);
    }

    private DeckStatisticsDTO calculateStatistics(List<WordProgressDTO> wordProgressDTOs) {
        Map<WordStatus, Integer> wordStatusCounts = wordProgressDTOs.stream()
                .collect(Collectors.groupingBy(WordProgressDTO::getWordStatus, Collectors.summingInt(wp -> 1)));

        int totalWords = wordProgressDTOs.size();
        int learningWords = wordStatusCounts.getOrDefault(WordStatus.LEARNING, 0);
        int repeatingWords = wordStatusCounts.getOrDefault(WordStatus.REPEATING, 0);
        int newWords = wordStatusCounts.getOrDefault(WordStatus.NEW, 0);

        return DeckStatisticsDTO.builder()
                .totalWords(totalWords)
                .repeatingWords(repeatingWords)
                .learningWords(learningWords)
                .newWords(newWords)
                .build();
    }
}