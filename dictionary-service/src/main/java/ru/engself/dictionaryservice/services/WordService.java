package ru.engself.dictionaryservice.services;

import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.WordDTO;

import java.util.Optional;
import java.util.UUID;

public interface WordService {
    WordDTO createWord(UUID deckId, WordDTO wordDTO, Optional<MultipartFile> file, UUID userId);

    WordDTO updateWordById(UUID wordId, WordDTO currentWord, Optional<MultipartFile> file, UUID userId);

    WordDTO getWordById(UUID wordId, UUID userId);
}
