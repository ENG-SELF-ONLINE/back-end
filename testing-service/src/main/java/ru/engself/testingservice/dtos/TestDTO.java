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
public class TestDTO {

    private UUID testId;

    private LessonDTO lesson;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
