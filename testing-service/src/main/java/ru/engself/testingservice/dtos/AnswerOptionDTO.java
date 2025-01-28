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
public class AnswerOptionDTO {

    private UUID answerOptionId;

    private QuestionDTO question;

    private String text;

    private Boolean isCorrect;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
