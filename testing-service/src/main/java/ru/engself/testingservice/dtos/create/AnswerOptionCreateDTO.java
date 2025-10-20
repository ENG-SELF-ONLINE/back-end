package ru.engself.testingservice.dtos.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnswerOptionCreateDTO {

    private String text;

    private Boolean isCorrect;

}
