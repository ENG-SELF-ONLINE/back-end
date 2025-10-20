package ru.engself.activitieslibrary.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.activitieslibrary.enums.ActivityType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserActivityDTO {

    private UUID userActivityId;

    private UUID userId;

    private ActivityType activityType;

    private String activityTitle;

    private LocalDateTime activityDate;

}
