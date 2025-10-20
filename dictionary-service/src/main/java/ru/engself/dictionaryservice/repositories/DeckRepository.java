package ru.engself.dictionaryservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.engself.dictionaryservice.entities.Deck;

import java.util.UUID;

public interface DeckRepository extends JpaRepository<Deck, UUID> {

    @Query(value = "SELECT * FROM decks WHERE (user_info->>'userId')::uuid = :userId",
            countQuery = "SELECT count(*) FROM decks WHERE (user_info->>'userId')::uuid = :userId",
            nativeQuery = true)
    Page<Deck> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

}
