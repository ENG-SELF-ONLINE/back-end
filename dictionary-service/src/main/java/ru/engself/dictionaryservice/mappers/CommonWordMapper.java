package ru.engself.dictionaryservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.dictionaryservice.dtos.CommonWordDTO;
import ru.engself.dictionaryservice.entities.CommonWord;

@Mapper(componentModel = "spring")
public interface CommonWordMapper {

    CommonWordDTO toDTO(CommonWord commonWord);

    CommonWord toEntity(CommonWordDTO commonWordDTO);

}
