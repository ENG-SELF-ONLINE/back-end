package ru.engself.dictionaryservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.entities.Deck;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    DeckDTO toDTO(Deck deck);

    Deck toEntity(DeckDTO deckDTO);

}
