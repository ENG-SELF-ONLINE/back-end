package ru.engself.dictionaryservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.engself.dictionaryservice.entities.CommonWord;

import java.util.Optional;
import java.util.UUID;

public interface CommonWordRepository extends JpaRepository<CommonWord, UUID> {

    Optional<CommonWord> findByWordName(String wordName);

}
