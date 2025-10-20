package ru.engself.readingservice.mappers;

import org.mapstruct.Mapper;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.entities.Book;

@Mapper(componentModel = "spring", uses = {DifficultWordMapper.class})
public interface BookMapper {

    BookDTO toDTO(Book book);

    Book toEntity(BookDTO bookDTO);

}
