package ru.engself.readingservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.engself.readingservice.dtos.DifficultWordDTO;
import ru.engself.readingservice.entities.DifficultWord;

@Mapper(componentModel = "spring")
public interface DifficultWordMapper {

    DifficultWordDTO toDTO(DifficultWord difficultWord);

    DifficultWord toEntity(DifficultWordDTO difficultWordDTO);

}
