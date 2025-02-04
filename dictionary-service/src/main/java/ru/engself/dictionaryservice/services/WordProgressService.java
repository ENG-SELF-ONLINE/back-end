package ru.engself.dictionaryservice.services;

import ru.engself.dictionaryservice.dtos.DeckStatisticsDTO;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.dtos.WordProgressDTO;
import ru.engself.dictionaryservice.enums.WordReviewResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WordProgressService {
    DeckStatisticsDTO getStatisticsByDeckId(UUID deckId, UUID userId);

    DeckStatisticsDTO getStatisticsByUserId(UUID userId);

    List<WordProgressDTO> getNextWords(UUID deckId, UUID userId);

    void updateWordProgress(UUID wordProgressId, WordReviewResult result, UUID userId);

    List<WordProgressDTO> getWordsByDeckId(UUID deckId, UUID userId);

    WordProgressDTO createWordProgress(WordDTO wordDTO, UUID userId);

    WordProgressDTO getWordProgressById(UUID wordProgressId, UUID userId);

    WordProgressDTO getWordProgressByWordId(UUID wordId, UUID userId);

    DeckStatisticsDTO getStatisticsByPeriod(LocalDateTime startDate, LocalDateTime endDate, UUID userId);
}
