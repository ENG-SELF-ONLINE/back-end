package ru.engself.dictionaryservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.dictionaryservice.dtos.WordProgressDTO;
import ru.engself.dictionaryservice.entities.WordProgress;

@Mapper(componentModel = "spring", uses = {WordMapper.class})
public interface WordProgressMapper {

    WordProgressDTO toDTO(WordProgress wordProgress);

    WordProgress toEntity(WordProgressDTO wordProgressDTO);

}
