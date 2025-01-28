package ru.engself.dictionaryservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.engself.dictionaryservice.dtos.WordDTO;
import ru.engself.dictionaryservice.entities.Word;

@Mapper(componentModel = "spring", uses = {CommonWordMapper.class})
public interface WordMapper {

    WordDTO toDTO(Word word);

    Word toEntity(WordDTO wordDTO);

}
