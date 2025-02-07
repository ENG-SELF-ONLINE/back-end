package ru.engself.trackerservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.engself.trackerservice.dtos.ActivityStatsDTO;
import ru.engself.trackerservice.dtos.ActivityTrackerDTO;
import ru.engself.trackerservice.dtos.DeckStatisticsDTO;
import ru.engself.trackerservice.dtos.UserDTO;
import ru.engself.trackerservice.entities.ActivityTracker;
import ru.engself.trackerservice.mappers.ActivityTrackerMapper;
import ru.engself.trackerservice.repositories.ActivityTrackerRepository;
import ru.engself.trackerservice.services.ActivityTrackerService;
import ru.engself.trackerservice.utils.feigns.DictionaryFeignController;
import ru.engself.trackerservice.utils.feigns.ProfileFeignController;

import javax.ws.rs.NotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.engself.trackerservice.utils.AuthenticationUtils.*;

@Service
@RequiredArgsConstructor
public class ActivityTrackerServiceImpl implements ActivityTrackerService {

    private final ActivityTrackerRepository activityTrackerRepository;
    private final ActivityTrackerMapper activityTrackerMapper;
    private final ProfileFeignController profileFeignController;
    private final DictionaryFeignController dictionaryFeignController;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public ActivityTrackerDTO createActivity(ActivityTrackerDTO activityDTO, Authentication authentication) {
        UserDTO user = profileFeignController.getUserById(getAuthorizationHeader(authentication));
        LocalDateTime startTime = activityDTO.getStartTime();
        LocalDateTime endTime = activityDTO.getEndTime();

        ActivityTrackerDTO activityTracker = ActivityTrackerDTO.builder()
                .userInfo(user)
                .activityType(activityDTO.getActivityType())
                .startTime(startTime)
                .endTime(endTime)
                .duration(Duration.between(startTime, endTime).toMinutes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        String keyPrefix = generateKeyPrefix("activity_stats", user.getUserId());
        kafkaTemplate.send("activity-updates", keyPrefix);

        return activityTrackerMapper.toDTO(
                activityTrackerRepository.save(activityTrackerMapper.toEntity(activityTracker))
        );
    }

    @Override
    public ActivityTrackerDTO getActivityById(UUID activityId, UUID userId) {
        return activityTrackerRepository.findById(activityId).map(activityTrackerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("There is no activity with activityId: " + activityId));
    }

    @Override
    public ActivityTrackerDTO updateActivityById(ActivityTrackerDTO activityDTO, UUID id, UUID userId) {

        ActivityTrackerDTO activityTracker = getActivityById(id, userId);

        LocalDateTime startTime = activityDTO.getStartTime();
        LocalDateTime endTime = activityDTO.getEndTime();

        if (startTime == null || endTime == null) {
            throw new NotFoundException("Start or End time must be not null");
        }

        activityTracker.setStartTime(startTime);
        activityTracker.setEndTime(endTime);
        activityTracker.setDuration(Duration.between(startTime, endTime).toMinutes());
        String keyPrefix = generateKeyPrefix("activity_stats", userId);
        kafkaTemplate.send("activity-updates", keyPrefix);

        return activityTrackerMapper.toDTO(
                activityTrackerRepository.save(activityTrackerMapper.toEntity(activityTracker))
        );
    }

    @Override
    public String deleteActivityById(UUID activityId, UUID userId) {

        if (activityTrackerRepository.findById(activityId).isEmpty()) {
            throw new EntityNotFoundException("There is no activity with activityId: " + activityId);
        }

        String keyPrefix = generateKeyPrefix("activity_stats", userId);
        kafkaTemplate.send("activity-updates", keyPrefix);
        activityTrackerRepository.deleteById(activityId);
        return "successful deleted";

    }

    @Override
    public List<ActivityTrackerDTO> getAllActivities(LocalDateTime startDate, LocalDateTime endDate, UUID userId) {
        List<ActivityTracker> activities = activityTrackerRepository
                .findActivitiesByUserIdAndDateRange(userId, startDate, endDate);

        return activities.stream()
                .map(activityTrackerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityStatsDTO getActivityStats(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);

        List<ActivityTracker> activities = activityTrackerRepository
                .findActivitiesByUserIdAndDateRange(userId, startDate, endDate);

        DeckStatisticsDTO deckStatistics = dictionaryFeignController.getStatisticsByPeriod(startDate, endDate, getAuthorizationHeader(authentication));

        Map<String, List<ActivityStatsDTO.ActivityValue>> activitiesMap = activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.getActivityType().name().toLowerCase(),
                        Collectors.mapping(activity -> ActivityStatsDTO.ActivityValue.builder()
                                        .date(activity.getStartTime().toString())
                                        .value(activity.getDuration().intValue())
                                        .build(),
                                Collectors.toList())
                ));

        double totalTime = activities.stream()
                .mapToDouble(activity -> activity.getDuration().doubleValue() / 60.0)
                .sum();

        return ActivityStatsDTO.builder()
                .activities(activitiesMap)
                .totalTime(totalTime)
                .deckStatisticsDTO(deckStatistics)
                .build();
    }

}
