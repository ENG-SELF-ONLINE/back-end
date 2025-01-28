package ru.engself.readingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.BookProgressDTO;
import ru.engself.readingservice.dtos.UserDTO;
import ru.engself.readingservice.entities.BookProgress;
import ru.engself.readingservice.enums.Level;
import ru.engself.readingservice.mappers.BookProgressMapper;
import ru.engself.readingservice.repositories.BookProgressRepository;
import ru.engself.readingservice.services.BookProgressService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookProgressServiceImpl implements BookProgressService {

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressMapper bookProgressMapper;


    @Override
    @Transactional
    public BookProgressDTO markBookAsCompleted(UUID bookId, UUID userId) {

        BookProgress bookProgress = bookProgressRepository.findByBookBookIdAndUserInfoUserId(bookId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Progress for book with id " + bookId + " not found"));

        bookProgress.setIsCompleted(true);
        bookProgress.setUpdatedAt(LocalDateTime.now());

        return bookProgressMapper.toDTO(bookProgressRepository.save(bookProgress));
    }


    @Override
    @Transactional
    public BookProgressDTO unmarkBookAsCompleted(UUID bookId, UUID userId) {

        BookProgress bookProgress = bookProgressRepository.findByBookBookIdAndUserInfoUserId(bookId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Progress for book with id " + bookId + " not found"));

        bookProgress.setIsCompleted(false);
        bookProgress.setUpdatedAt(LocalDateTime.now());

        return bookProgressMapper.toDTO(bookProgressRepository.save(bookProgress));
    }

    @Override
    @Transactional
    public BookProgressDTO createBookProgress(BookDTO bookDTO, UUID userId) {

        BookProgressDTO bookProgress = BookProgressDTO.builder()
                .book(bookDTO)
                .isCompleted(false)
                .userInfo(
                        UserDTO.builder()
                                .userId(userId)
                                .build()
                )
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return bookProgressMapper.toDTO(
                bookProgressRepository.save(bookProgressMapper.toEntity(bookProgress))
        );
    }

    @Override
    public List<BookProgressDTO> getAllBookProgressesByUserId(UUID userId) {
        return bookProgressRepository.findBookProgressesByUserInfoUserId(userId).stream()
                .map(bookProgressMapper::toDTO).toList();
    }

    @Override
    public Integer getBookProgressPercentByUserId(UUID userId) {
        List<BookProgressDTO> bookProgresses = getAllBookProgressesByUserId(userId);

//        UserDTO userInfo = UserDTO.builder()
//                .userId(userId)
//                .build();

//        if (userInfo == null || userInfo.getLevel() == null) {
//            return 0; // Return 0% if user level or userInfo is null
//        }

        Level userLevel = Level.B1;

        long totalBooksOnLevel = bookProgresses.stream()
                .map(BookProgressDTO::getBook)
                .filter(book -> book.getLevel() == userLevel)
                .count();

        if (totalBooksOnLevel == 0) {
            return 0; // Return 0% if there are no books on user's level
        }

        long completedBooksOnLevel = bookProgresses.stream()
                .filter(BookProgressDTO::getIsCompleted)
                .map(BookProgressDTO::getBook)
                .filter(book -> book.getLevel() == userLevel)
                .count();

        return (int) Math.round((double) completedBooksOnLevel / totalBooksOnLevel * 100);
    }

}
