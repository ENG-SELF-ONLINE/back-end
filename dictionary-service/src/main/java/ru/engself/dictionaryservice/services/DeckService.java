package ru.engself.dictionaryservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.DeckDTO;

import java.util.UUID;

public interface DeckService {

    DeckDTO createDeck(String name, MultipartFile file, Authentication authentication);

    DeckDTO getDeckById(UUID deckId, UUID userId);

    Page<DeckDTO> getAllDecksByUserId(UUID userId, Pageable pageable);

    DeckDTO updateDeckById(UUID deckId, DeckDTO deckDTO, UUID userId);

    String deleteDeckById(UUID deckId, UUID userId);

}
