package ru.engself.dictionaryservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.DeckDTO;
import ru.engself.dictionaryservice.dtos.UserDTO;
import ru.engself.dictionaryservice.entities.Deck;
import ru.engself.dictionaryservice.enums.BucketEnum;
import ru.engself.dictionaryservice.exceptions.DeckNotFoundException;
import ru.engself.dictionaryservice.mappers.DeckMapper;
import ru.engself.dictionaryservice.repositories.DeckRepository;
import ru.engself.dictionaryservice.services.DeckService;
import ru.engself.dictionaryservice.utils.feigns.PhotoFeignController;
import ru.engself.dictionaryservice.utils.feigns.ProfileFeignController;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.engself.dictionaryservice.utils.AuthenticationUtils.generateKeyPrefix;
import static ru.engself.dictionaryservice.utils.AuthenticationUtils.getAuthorizationHeader;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;
    private final PhotoFeignController photoFeignController;
    private final ProfileFeignController profileFeignController;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public DeckDTO createDeck(String name, MultipartFile file, Authentication authentication) {

        String photoName = photoFeignController.uploadFile(file, BucketEnum.DECKS).getFilename();
        UserDTO user = profileFeignController.getUserById(getAuthorizationHeader(authentication));

        DeckDTO deck = DeckDTO.builder()
                .deckName(name)
                .userInfo(user)
                .deckPhoto(photoName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return deckMapper.toDTO(deckRepository.save(deckMapper.toEntity(deck)));
    }

    @Override
    public DeckDTO getDeckById(UUID deckId, UUID userId) {
        return deckRepository.findById(deckId)
                .map(deckMapper::toDTO)
                .orElseThrow(() -> new DeckNotFoundException("There is no deck with id: " + deckId));
    }

    @Override
    public Page<DeckDTO> getAllDecksByUserId(UUID userId, Pageable pageable) {
        Page<Deck> decks = deckRepository.findAllByUserId(userId, pageable);
        return decks.map(deckMapper::toDTO);
    }

    @Override
    @Transactional
    public DeckDTO updateDeckById(UUID deckId, DeckDTO deckDTO, UUID userId) {

        DeckDTO deck = getDeckById(deckId, userId);

        if (deckDTO.getDeckName() != null && !deckDTO.getDeckName().isBlank()) {
            deck.setDeckName(deckDTO.getDeckName());
        }

        if (deckDTO.getDeckPhoto() != null && !deckDTO.getDeckPhoto().isBlank()) {
            deck.setDeckPhoto(deckDTO.getDeckPhoto());
        }
        deck.setUpdatedAt(LocalDateTime.now());

        return deckMapper.toDTO(deckRepository.save(deckMapper.toEntity(deck)));
    }

    @Override
    public String deleteDeckById(UUID deckId, UUID userId) {

        if (deckRepository.findById(deckId).isEmpty()) {
            throw new DeckNotFoundException("There is no deck with id: " + deckId);
        }

        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_period", userId));
        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_deck", userId));
        kafkaTemplate.send("word-progress-updates", generateKeyPrefix("deck_statistics_user", userId));
        kafkaTemplate.send("activity-updates", generateKeyPrefix("activity_stats", userId));

        deckRepository.deleteById(deckId);
        return "successful deleted";

    }

}