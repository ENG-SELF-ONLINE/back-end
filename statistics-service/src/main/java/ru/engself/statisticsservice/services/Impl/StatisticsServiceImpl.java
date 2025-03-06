package ru.engself.statisticsservice.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.engself.statisticsservice.dtos.ActivityStatsDTO;
import ru.engself.statisticsservice.dtos.DeckStatisticsDTO;
import ru.engself.statisticsservice.dtos.NotificationDTO;
import ru.engself.statisticsservice.enums.LessonType;
import ru.engself.statisticsservice.enums.NotificationType;
import ru.engself.statisticsservice.services.RedisService;
import ru.engself.statisticsservice.services.StatisticsService;
import ru.engself.statisticsservice.utils.feigns.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.engself.statisticsservice.utils.AuthenticationUtils.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ActivityTrackerFeignController activityTrackerFeignController;
    private final DictionaryFeignController dictionaryFeignController;
    private final ReadingFeignController readingFeignController;
    private final TestingFeignController testingFeignController;
    private final ProfileFeignController profileFeignController;
    private final RedisService redisService;


    @Override
    public DeckStatisticsDTO getStatisticsByPeriod(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("deck_statistics_period", userId, startDate, endDate);

        DeckStatisticsDTO deckStatistics = (DeckStatisticsDTO) redisService.getCachedStats(key);

        if (deckStatistics == null) {
            deckStatistics = dictionaryFeignController.getStatisticsByPeriod(startDate, endDate, authorizationHeader);
            redisService.cacheStats(key, deckStatistics);
        }

        return deckStatistics;
    }

    @Override
    public DeckStatisticsDTO getStatisticsByDeckId(UUID deckId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("deck_statistics_deck", userId, deckId);

        DeckStatisticsDTO deckStatistics = (DeckStatisticsDTO) redisService.getCachedStats(key);

        if (deckStatistics == null) {
            deckStatistics = dictionaryFeignController.getStatisticsByDeckId(deckId, authorizationHeader);
            redisService.cacheStats(key, deckStatistics);
        }

        return deckStatistics;
    }

    @Override
    public DeckStatisticsDTO getStatisticsByUserId(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("deck_statistics_user", userId);

        DeckStatisticsDTO deckStatistics = (DeckStatisticsDTO) redisService.getCachedStats(key);

        if (deckStatistics == null) {
            deckStatistics = dictionaryFeignController.getStatisticsByUserId(authorizationHeader);
            redisService.cacheStats(key, deckStatistics);
        }

        return deckStatistics;
    }

    @Override
    public Integer getBookProgressPercentByUserId(UUID friendId, Authentication authentication) {

        UUID userId;

        if (friendId != null) {
            userId = friendId;
        } else userId = getUserIdFromAuthentication(authentication);

        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("book_progress", userId);

        Integer percent = (Integer) redisService.getCachedStats(key);

        if (percent == null) {
            percent = readingFeignController.getBookProgressPercentByUserId(userId, authorizationHeader);
            redisService.cacheStats(key, percent);
        }

        return percent;
    }

    @Override
    public Integer getBookProgressPercentByUserIdAndType(UUID friendId, LessonType type, Authentication authentication) {

        UUID userId;

        if (friendId != null) {
            userId = friendId;
        } else userId = getUserIdFromAuthentication(authentication);

        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("test_progress_type", userId, type);

        Integer percent = (Integer) redisService.getCachedStats(key);

        if (percent == null) {
            percent = testingFeignController.getBookProgressPercentByUserIdAndType(userId, type, authorizationHeader);
            redisService.cacheStats(key, percent);
        }

        return percent;
    }

    @Override
    public ActivityStatsDTO getActivityStats(UUID friendId, LocalDateTime startDate, LocalDateTime endDate, Authentication authentication) {

        UUID userId;

        if (friendId != null) {
            userId = friendId;
        } else userId = getUserIdFromAuthentication(authentication);

        String authorizationHeader = getAuthorizationHeader(authentication);
        String key = generateKey("activity_stats", userId, startDate, endDate);

        ActivityStatsDTO activityStats = (ActivityStatsDTO) redisService.getCachedStats(key);

        if (activityStats == null) {
            activityStats = activityTrackerFeignController.getActivityStats(userId, startDate, endDate, authorizationHeader);
            redisService.cacheStats(key, activityStats);
        }

        return activityStats;
    }

    @Override
    public Integer getProgressBarByUserId(UUID friendId, Authentication authentication) {

        UUID userId;

        if (friendId != null) {
            userId = friendId;
        } else userId = getUserIdFromAuthentication(authentication);

        Integer bookProgress = getBookProgressPercentByUserId(userId, authentication);
        Integer listeningProgress = getBookProgressPercentByUserIdAndType(userId, LessonType.LISTENING, authentication);
        Integer grammarProgress = getBookProgressPercentByUserIdAndType(userId, LessonType.GRAMMAR, authentication);

        int totalProgress = (bookProgress + listeningProgress + grammarProgress) / 3;

        if (friendId == null && totalProgress > 80 && !hasHighProgressNotification(userId, authentication)) {
            sendHighProgressNotification(authentication);
        }

        return totalProgress;
    }

    private boolean hasHighProgressNotification(UUID userId, Authentication authentication) {
        NotificationDTO notificationDTO = profileFeignController.getNotificationByRecipientIdAndType(userId,
                NotificationType.PROGRESS_UPDATE, getAuthorizationHeader(authentication));

        return notificationDTO != null;
    }

    private void sendHighProgressNotification(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(profileFeignController.getUserById(getAuthorizationHeader(authentication)))
                .sender(null)
                .type(NotificationType.PROGRESS_UPDATE)
                .message("Поздравляем! Ваш общий прогресс достиг 80%! Вы можете перейти на новый уровень.")
                .contextId(UUID.randomUUID())
                .isRead(false)
                .build();

        profileFeignController.createNotification(userId, userId, notificationDTO, getAuthorizationHeader(authentication));
    }
}
