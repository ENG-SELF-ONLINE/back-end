package ru.engself.testingservice.dtos.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class QuestionCreateDTO {

    private String text;

    private List<AnswerOptionCreateDTO> answerOptions;

}
