package ru.engself.trackerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.engself.trackerservice.entities.ActivityTracker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ActivityTrackerRepository extends JpaRepository<ActivityTracker, UUID> {

    @Query(value = "SELECT activity_tracker_id, user_info, activity_type, start_time, end_time, duration, created_at, updated_at FROM activity_trackers WHERE (user_info->>'userId')::uuid = :userId AND start_time >= :startDate AND end_time <= :endDate", nativeQuery = true)
    List<ActivityTracker> findActivitiesByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate);


}
