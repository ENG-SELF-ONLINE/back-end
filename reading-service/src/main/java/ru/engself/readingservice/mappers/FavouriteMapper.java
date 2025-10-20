package ru.engself.readingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.readingservice.dtos.FavouriteDTO;
import ru.engself.readingservice.entities.Favourite;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface FavouriteMapper {

    FavouriteDTO toDTO(Favourite favourite);

    Favourite toEntity(FavouriteDTO favouriteDTO);

}
