package ru.engself.trackerservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.trackerservice.enums.ActivityType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ActivityTrackerDTO {

    private UUID activityTrackerId;

    private UserDTO userInfo;

    private ActivityType activityType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long duration;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
