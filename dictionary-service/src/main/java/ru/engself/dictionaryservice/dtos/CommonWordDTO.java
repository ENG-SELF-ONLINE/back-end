package ru.engself.dictionaryservice.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class CommonWordDTO {

    private UUID commonWordId;

    private String wordName;

    private String wordTranscription;

    private String wordTranslation;

    private String wordPhoto;

    private LocalDateTime createdAt;

}
