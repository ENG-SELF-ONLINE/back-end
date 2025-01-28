package ru.engself.readingservice.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.enums.Level;

import java.util.UUID;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO, MultipartFile file, MultipartFile image, UUID userId);

    BookDTO getBookById(UUID bookId, UUID userId);

    Page<BookDTO> getAllByLevel(Level level, UUID userId, Pageable pageable);

    BookDTO updateBookById(UUID bookId, BookDTO bookDTO, UUID userId);

    String deleteBookById(UUID bookId, UUID userId);

    ByteArrayResource downloadBook(UUID bookId, UUID userId);
}
