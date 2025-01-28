package ru.engself.testingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.testingservice.dtos.QuestionDTO;
import ru.engself.testingservice.entities.Question;

@Mapper(componentModel = "spring", uses = {TestMapper.class})
public interface QuestionMapper {

    QuestionDTO toDTO(Question question);

    Question toEntity(QuestionDTO questionDTO);

}
