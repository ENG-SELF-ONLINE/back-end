package ru.engself.readingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.engself.readingservice.entities.BookProgress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookProgressRepository extends JpaRepository<BookProgress, UUID> {

    @Query(value = "SELECT * FROM book_progress WHERE book_id = :bookId AND (user_info->>'userId')::uuid = :userId", nativeQuery = true)
    Optional<BookProgress> findByBookBookIdAndUserInfoUserId(UUID bookId, UUID userId);

    @Query(value = "SELECT * FROM book_progress WHERE (user_info->>'userId')::uuid = :userId", nativeQuery = true)
    List<BookProgress> findBookProgressesByUserInfoUserId(UUID userId);

}
