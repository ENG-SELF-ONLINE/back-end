package ru.engself.activitieslibrary.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.engself.activitieslibrary.dtos.UserActivityDTO;
import ru.engself.activitieslibrary.entities.UserActivity;
import ru.engself.activitieslibrary.enums.ActivityType;
import ru.engself.activitieslibrary.mappers.UserActivityMapper;
import ru.engself.activitieslibrary.repositories.UserActivityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserActivityMapper userActivityMapper;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserActivityService.class);

    public void saveActivity(UUID userId, ActivityType activityType, String activityTitle) {
        UserActivity activity = UserActivity.builder()
                .userId(userId)
                .activityType(activityType)
                .activityTitle(activityTitle)
                .activityDate(LocalDateTime.now())
                .build();

        LOGGER.info("Saving activity: " + activity);
        userActivityRepository.save(activity);
    }

    public List<UserActivityDTO> getLastThreeActivities(UUID userId) {
        List<UserActivity> activities = userActivityRepository.findTop3ByUserIdOrderByActivityDateDesc(userId);
        return activities.stream()
                .map(userActivityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldActivities() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        userActivityRepository.deleteByActivityDateBefore(oneWeekAgo);
    }
}
