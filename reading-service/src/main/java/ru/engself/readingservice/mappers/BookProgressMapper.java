package ru.engself.readingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.readingservice.dtos.BookProgressDTO;
import ru.engself.readingservice.entities.BookProgress;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookProgressMapper {

    BookProgressDTO toDTO(BookProgress bookProgress);

    BookProgress toEntity(BookProgressDTO bookProgressDTO);

}
