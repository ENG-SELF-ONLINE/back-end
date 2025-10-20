package ru.engself.readingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.readingservice.entities.DifficultWord;

import java.util.UUID;

public interface DifficultWordRepository extends JpaRepository<DifficultWord, UUID> {
}
