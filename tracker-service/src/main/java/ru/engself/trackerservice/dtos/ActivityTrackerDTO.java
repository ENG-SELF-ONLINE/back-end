package ru.engself.trackerservice.dtos;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import ru.engself.trackerservice.enums.ActivityType;

import java.time.LocalDate;
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
