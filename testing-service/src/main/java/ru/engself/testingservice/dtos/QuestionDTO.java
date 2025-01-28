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
public class QuestionDTO {

    private UUID questionId;

    private TestDTO test;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
