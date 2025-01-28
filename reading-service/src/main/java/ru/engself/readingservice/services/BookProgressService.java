package ru.engself.readingservice.services;

import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.BookProgressDTO;

import java.util.List;
import java.util.UUID;

public interface BookProgressService {

    BookProgressDTO markBookAsCompleted(UUID bookId, UUID userId);

    BookProgressDTO unmarkBookAsCompleted(UUID bookId, UUID userId);

    BookProgressDTO createBookProgress(BookDTO bookDTO, UUID userId);

    List<BookProgressDTO> getAllBookProgressesByUserId(UUID userId);

    Integer getBookProgressPercentByUserId(UUID userId);
}
