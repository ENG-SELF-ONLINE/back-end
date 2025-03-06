package ru.engself.readingservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.entities.Book;
import ru.engself.readingservice.enums.BucketEnum;
import ru.engself.readingservice.enums.Genre;
import ru.engself.readingservice.enums.Level;
import ru.engself.readingservice.exceptions.BookNotFoundException;
import ru.engself.readingservice.mappers.BookMapper;
import ru.engself.readingservice.repositories.BookRepository;
import ru.engself.readingservice.services.BookProgressService;
import ru.engself.readingservice.services.BookService;
import ru.engself.readingservice.utils.feigns.PhotoFeignController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.engself.readingservice.utils.AuthenticationUtils.generateKeyPrefix;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final PhotoFeignController photoFeignController;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookProgressService bookProgressService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO, MultipartFile file, MultipartFile image, Authentication authentication) {

        String bookName = photoFeignController.uploadFile(file, BucketEnum.BOOKS).getFilename();
        String imageName = photoFeignController.uploadFile(image, BucketEnum.COVERS).getFilename();

        bookDTO.setBookFile(bookName);
        bookDTO.setCoverImage(imageName);
        bookDTO.setCreatedAt(LocalDateTime.now());
        bookDTO.setUpdatedAt(LocalDateTime.now());

        Book book = bookRepository.save(bookMapper.toEntity(bookDTO));
        bookProgressService.createBookProgress(bookMapper.toDTO(book), authentication);

        return bookMapper.toDTO(book);
    }

    @Override
    public BookDTO getBookById(UUID bookId, UUID userId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new BookNotFoundException("There is no deck with id: " + bookId));
    }

    @Override
    public Page<BookDTO> getAllByLevel(Level level, UUID userId, Pageable pageable) {
        // Получаем страницу книг из репозитория
        Page<Book> books = bookRepository.findAllByLevel(level, pageable);

        // Преобразуем Page<Book> в Page<BookDTO>
        Page<BookDTO> bookDTOs = books.map(bookMapper::toDTO);

        System.out.println("Content: " + bookDTOs);

        return bookDTOs;
    }

    @Override
    public BookDTO updateBookById(UUID bookId, BookDTO bookDTO, UUID userId) {

        BookDTO book = getBookById(bookId, userId);

        String title = bookDTO.getTitle();
        String author = bookDTO.getAuthor();
        String description = bookDTO.getDescription();
        String bookFile = bookDTO.getBookFile();
        String coverImage = bookDTO.getCoverImage();
        Genre genre = bookDTO.getGenre();
        Level level = bookDTO.getLevel();

        if (title != null && !title.isBlank()) {
            book.setTitle(title);
        }
        if (author != null && !author.isBlank()) {
            book.setAuthor(author);
        }
        if (description != null) {
            book.setDescription(description);
        }
        if (bookFile != null && !bookFile.isBlank()) {
            book.setBookFile(bookFile);
        }
        if (coverImage != null && !coverImage.isBlank()) {
            book.setCoverImage(coverImage);
        }
        if (genre != null) {
            book.setGenre(genre);
        }
        if (level != null) {
            book.setLevel(level);
        }

        book.setUpdatedAt(LocalDateTime.now());
        String keyPrefix = generateKeyPrefix("book_progress", userId);
        kafkaTemplate.send("reading-updates", keyPrefix);

        return bookMapper.toDTO(bookRepository.save(bookMapper.toEntity(book)));

    }

    @Override
    public String deleteBookById(UUID bookId, UUID userId) {

        if (bookRepository.findById(bookId).isEmpty()) {
            throw new BookNotFoundException("There is no book with id: " + bookId);
        }

        String keyPrefix = generateKeyPrefix("book_progress", userId);
        kafkaTemplate.send("reading-updates", keyPrefix);

        bookRepository.deleteById(bookId);
        return "successful deleted";

    }

    @Override
    @Transactional
    public ByteArrayResource downloadBook(UUID bookId, UUID userId) {
        BookDTO book = getBookById(bookId, userId);
        incrementDownloads(bookId, userId);

        return photoFeignController.downloadPdf(book.getBookFile(), BucketEnum.BOOKS);
    }

    @Override
    @Transactional
    public void incrementDownloads(UUID bookId, UUID userId) {
        BookDTO book = getBookById(bookId, userId);
        book.setDownloads(book.getDownloads() + 1);

        bookRepository.save(
                bookMapper.toEntity(book)
        );
    }

    @Override
    @Transactional
    public void incrementFavourites(UUID bookId, UUID userId) {
        BookDTO book = getBookById(bookId, userId);
        book.setFavourites(book.getFavourites() + 1);

        bookRepository.save(
                bookMapper.toEntity(book)
        );
    }

    @Override
    @Transactional
    public void decrementFavourites(UUID bookId, UUID userId) {
        BookDTO book = getBookById(bookId, userId);
        book.setFavourites(book.getFavourites() - 1);

        bookRepository.save(
                bookMapper.toEntity(book)
        );
    }

    @Override
    public List<BookDTO> getAllByLevelWithoutPage(Level level, UUID userId) {
        return bookRepository.findAllByLevel(level).stream()
                .map(bookMapper::toDTO).toList();
    }
}
