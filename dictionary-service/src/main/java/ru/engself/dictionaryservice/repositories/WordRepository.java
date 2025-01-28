package ru.engself.dictionaryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.dictionaryservice.entities.Word;

import java.util.UUID;

public interface WordRepository extends JpaRepository<Word, UUID> {
}
