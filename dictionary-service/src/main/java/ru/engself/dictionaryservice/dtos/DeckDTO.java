package ru.engself.dictionaryservice.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class DeckDTO {

    private UUID deckId;

    private String deckName;

    private String deckPhoto;

    private UserDTO userInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
