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

    Integer getBookProgressPercentByUserId(Authentication authentication);

    Integer getBookProgressPercentByUserIdAndType(LessonType type, Authentication authentication);

    ActivityStatsDTO getActivityStats(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);
}
