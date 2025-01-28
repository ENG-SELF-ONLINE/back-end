package ru.engself.testingservice.dtos.create;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TestCreateDTO {

    private List<QuestionCreateDTO> questions;

}
