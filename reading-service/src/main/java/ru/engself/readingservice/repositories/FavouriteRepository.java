package ru.engself.readingservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.readingservice.entities.Favourite;

import java.util.Optional;
import java.util.UUID;

public interface FavouriteRepository extends JpaRepository<Favourite, UUID> {

    Optional<Favourite> findByUserIdAndBookBookId(UUID userId, UUID bookId);

    Page<Favourite> findAllByUserId(UUID userId, Pageable pageable);

}
