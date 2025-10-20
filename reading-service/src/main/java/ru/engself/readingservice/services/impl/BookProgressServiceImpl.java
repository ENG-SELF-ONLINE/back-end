package ru.engself.readingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.engself.activitieslibrary.aspects.TrackActivity;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.BookProgressDTO;
import ru.engself.readingservice.dtos.UserDTO;
import ru.engself.readingservice.entities.BookProgress;
import ru.engself.readingservice.mappers.BookProgressMapper;
import ru.engself.readingservice.repositories.BookProgressRepository;
import ru.engself.readingservice.services.BookProgressService;
import ru.engself.readingservice.utils.feigns.ProfileFeignController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.engself.readingservice.utils.AuthenticationUtils.generateKeyPrefix;
import static ru.engself.readingservice.utils.AuthenticationUtils.getAuthorizationHeader;

@Service
@RequiredArgsConstructor
public class BookProgressServiceImpl implements BookProgressService {

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressMapper bookProgressMapper;
    private final ProfileFeignController profileFeignController;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @Transactional
    @TrackActivity(
            userId = "#userId",
            activityType = "#service.getActivityType(#bookId)",
            activityTitle = "#service.getActivityTitle(#bookId)"
    )
    public BookProgressDTO markBookAsCompleted(UUID bookId, UUID userId) {

        BookProgress bookProgress = bookProgressRepository.findByBookBookIdAndUserInfoUserId(bookId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Progress for book with id " + bookId + " not found"));

        bookProgress.setIsCompleted(true);
        bookProgress.setUpdatedAt(LocalDateTime.now());

        String keyPrefix = generateKeyPrefix("book_progress", userId);
        kafkaTemplate.send("reading-updates", keyPrefix);
        return bookProgressMapper.toDTO(bookProgressRepository.save(bookProgress));
    }


    @Override
    @Transactional
    public BookProgressDTO unmarkBookAsCompleted(UUID bookId, UUID userId) {

        BookProgress bookProgress = bookProgressRepository.findByBookBookIdAndUserInfoUserId(bookId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Progress for book with id " + bookId + " not found"));

        bookProgress.setIsCompleted(false);
        bookProgress.setUpdatedAt(LocalDateTime.now());

        String keyPrefix = generateKeyPrefix("book_progress", userId);
        kafkaTemplate.send("reading-updates", keyPrefix);
        return bookProgressMapper.toDTO(bookProgressRepository.save(bookProgress));
    }

    @Override
    @Transactional
    public BookProgressDTO createBookProgress(BookDTO bookDTO, Authentication authentication) {

        UserDTO user = profileFeignController.getUserById(null, getAuthorizationHeader(authentication));

        Optional<BookProgress> progressOptional = bookProgressRepository.findByBookBookIdAndUserInfoUserId(
                    bookDTO.getBookId(), user.getUserId());

        if (progressOptional.isPresent()) {
            return bookProgressMapper.toDTO(progressOptional.get());
        }

        BookProgressDTO bookProgress = BookProgressDTO.builder()
                .book(bookDTO)
                .isCompleted(false)
                .userInfo(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String keyPrefix = generateKeyPrefix("book_progress", user.getUserId());
        kafkaTemplate.send("reading-updates", keyPrefix);
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
    public Integer getBookProgressPercentByUserId(UUID userId, Authentication authentication) {

        UserDTO user = profileFeignController.getUserById(userId, getAuthorizationHeader(authentication));
        List<BookProgressDTO> bookProgresses = getAllBookProgressesByUserId(user.getUserId());

        if (user.getLevel() == null) {
            return 0;
        }

        long totalBooksOnLevel = bookProgresses.stream()
                .map(BookProgressDTO::getBook)
                .filter(book -> book.getLevel() == user.getLevel())
                .count();

        if (totalBooksOnLevel == 0) {
            return 0;
        }

        long completedBooksOnLevel = bookProgresses.stream()
                .filter(BookProgressDTO::getIsCompleted)
                .map(BookProgressDTO::getBook)
                .filter(book -> book.getLevel() == user.getLevel())
                .count();

        return (int) Math.round((double) completedBooksOnLevel / totalBooksOnLevel * 100);
    }

    @Override
    public BookProgressDTO getBookProgressByBookId(UUID bookId, Authentication authentication) {
        return bookProgressRepository.findBookProgressByBookBookId(bookId).map(bookProgressMapper::toDTO).orElseThrow(
                () -> new EntityNotFoundException("There is no BookProgress with bookId: " + bookId)
        );
    }

    public String getActivityType(UUID bookId) {
        return "READING";
    }

    public String getActivityTitle(UUID bookId) {
        BookProgressDTO resultDTO = getBookProgressByBookId(bookId);
        return resultDTO.getBook().getTitle();
    }

    private BookProgressDTO getBookProgressByBookId(UUID bookId) {
        BookProgress bookProgress = bookProgressRepository
                .findFirstByBookBookIdOrderByCreatedAtDesc(bookId)
                .orElseThrow(() -> new EntityNotFoundException("There is no BookProgress with bookId: " + bookId));

        return bookProgressMapper.toDTO(bookProgress);
    }

}
