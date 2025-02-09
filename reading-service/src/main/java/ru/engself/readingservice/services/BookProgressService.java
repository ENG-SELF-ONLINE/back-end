package ru.engself.readingservice.services;

import org.springframework.security.core.Authentication;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.BookProgressDTO;

import java.util.List;
import java.util.UUID;

public interface BookProgressService {

    BookProgressDTO markBookAsCompleted(UUID bookId, UUID userId);

    BookProgressDTO unmarkBookAsCompleted(UUID bookId, UUID userId);

    BookProgressDTO createBookProgress(BookDTO bookDTO, Authentication authentication);

    List<BookProgressDTO> getAllBookProgressesByUserId(UUID userId);

    Integer getBookProgressPercentByUserId(Authentication authentication);

    BookProgressDTO getBookProgressByBookId(UUID bookId, Authentication authentication);
}
