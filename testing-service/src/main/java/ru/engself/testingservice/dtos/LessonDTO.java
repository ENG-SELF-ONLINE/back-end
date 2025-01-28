package ru.engself.testingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.testingservice.enums.LessonType;
import ru.engself.testingservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LessonDTO {

    private UUID lessonId;

    private String title;

    private Level level;

    private LessonType type;

    private Integer lessonOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
