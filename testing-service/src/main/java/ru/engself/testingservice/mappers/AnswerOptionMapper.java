package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.AnswerOptionDTO;
import ru.engself.testingservice.entities.AnswerOption;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface AnswerOptionMapper {

    AnswerOptionDTO toDTO(AnswerOption answerOption);

    AnswerOption toEntity(AnswerOptionDTO answerOptionDTO);
}
