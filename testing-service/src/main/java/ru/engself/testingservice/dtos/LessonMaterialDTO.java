package ru.engself.testingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LessonMaterialDTO {

    private UUID lessonMaterialsId;

    private LessonDTO lesson;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
