package ru.engself.dictionaryservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class WordDTO {

    private UUID wordId;

    private CommonWordDTO commonWord;

    @JsonIgnore
    private DeckDTO deck;

    private String wordTranslation;

    private String wordPhoto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
