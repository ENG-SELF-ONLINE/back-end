package ru.engself.statisticsservice.services;

import org.springframework.security.core.Authentication;
import ru.engself.statisticsservice.dtos.ActivityStatsDTO;
import ru.engself.statisticsservice.dtos.DeckStatisticsDTO;
import ru.engself.statisticsservice.enums.LessonType;

import java.time.LocalDateTime;
import java.util.UUID;

public interface StatisticsService {
    DeckStatisticsDTO getStatisticsByPeriod(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);

    DeckStatisticsDTO getStatisticsByDeckId(UUID deckId, Authentication authentication);

    DeckStatisticsDTO getStatisticsByUserId(Authentication authentication);

    Integer getBookProgressPercentByUserId(UUID friendId, Authentication authentication);

    Integer getBookProgressPercentByUserIdAndType(UUID friendId, LessonType type, Authentication authentication);

    ActivityStatsDTO getActivityStats(UUID friendId, LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);

    Integer getProgressBarByUserId(UUID friendId, Authentication authentication);
}
