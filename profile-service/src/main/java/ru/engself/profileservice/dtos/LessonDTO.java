package ru.engself.profileservice.dtos;

import lombok.*;
import ru.engself.profileservice.enums.LessonType;
import ru.engself.profileservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {

    private UUID lessonId;

    private String title;

    private Level level;

    private LessonType type;

    private Integer lessonOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
