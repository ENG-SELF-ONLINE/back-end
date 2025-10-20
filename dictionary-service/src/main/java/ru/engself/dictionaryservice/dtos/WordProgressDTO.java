package ru.engself.dictionaryservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.dictionaryservice.enums.WordReviewResult;
import ru.engself.dictionaryservice.enums.WordStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WordProgressDTO {

    private UUID wordProgressId;

    private WordDTO word;

    private WordStatus wordStatus;

    private LocalDateTime nextReviewDate;

    private WordReviewResult previousResult;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
