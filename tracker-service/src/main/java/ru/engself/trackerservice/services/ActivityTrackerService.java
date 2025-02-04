package ru.engself.trackerservice.services;

import org.springframework.security.core.Authentication;
import ru.engself.trackerservice.dtos.ActivityStatsDTO;
import ru.engself.trackerservice.dtos.ActivityTrackerDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ActivityTrackerService {
    ActivityTrackerDTO createActivity(ActivityTrackerDTO activityDTO, Authentication authentication);

    ActivityTrackerDTO getActivityById(UUID id, UUID userId);

    ActivityTrackerDTO updateActivityById(ActivityTrackerDTO activityDTO, UUID id, UUID userId);

    String deleteActivityById(UUID id, UUID userId);

    List<ActivityTrackerDTO> getAllActivities(LocalDateTime startDate, LocalDateTime endDate, UUID userId);

    ActivityStatsDTO getActivityStats(LocalDateTime startDate, LocalDateTime endDate, Authentication authentication);
}
