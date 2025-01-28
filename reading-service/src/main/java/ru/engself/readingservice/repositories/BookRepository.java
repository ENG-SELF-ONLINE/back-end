package ru.engself.readingservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.readingservice.entities.Book;
import ru.engself.readingservice.enums.Level;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    Page<Book> findAllByLevel(Level level, Pageable pageable);

}
