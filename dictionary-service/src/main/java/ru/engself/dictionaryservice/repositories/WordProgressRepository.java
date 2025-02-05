package ru.engself.dictionaryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.engself.dictionaryservice.entities.WordProgress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WordProgressRepository extends JpaRepository<WordProgress, UUID> {

    @Query("SELECT wp FROM WordProgress wp " +
            "JOIN wp.word uw " +
            "WHERE uw.deck.deckId = :deckId")
    List<WordProgress> findAllWordProgressesByDeckId(@Param("deckId") UUID deckId);

    @Query(value = "SELECT wp.* FROM word_progresses wp " +
            "JOIN words w ON wp.word_id = w.word_id " +
            "JOIN decks d ON w.deck_id = d.deck_id " +
            "WHERE (d.user_info->>'userId')::uuid = :userId", nativeQuery = true)
    List<WordProgress> findAllWordProgressesByUserId(@Param("userId") UUID userId);

    @Query("SELECT wp FROM WordProgress wp " +
            "WHERE wp.word.deck.deckId = :deckId AND (wp.nextReviewDate <= :now OR wp.nextReviewDate IS NULL) " +
            "ORDER BY wp.nextReviewDate ASC")
    List<WordProgress> findNextWordsByDeckId(@Param("deckId") UUID deckId, @Param("now") LocalDateTime now);

    List<WordProgress> findByWordDeckDeckId(UUID deckId);

    Optional<WordProgress> findWordProgressByWordWordId(@Param("wordId") UUID wordId);

    @Query(value = "SELECT wp.* FROM word_progresses wp " +
            "JOIN words w ON wp.word_id = w.word_id " +
            "JOIN decks d ON w.deck_id = d.deck_id " +
            "WHERE (d.user_info->>'userId')::uuid = :userId AND wp.updated_at >= :startDate AND wp.updated_at <= :endDate", nativeQuery = true)
    List<WordProgress> findAllWordProgressesByUserIdAndDateRange(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
