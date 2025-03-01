package ru.engself.readingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.readingservice.enums.Genre;
import ru.engself.readingservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookDTO {

    private UUID bookId;

    private String title;

    private String author;

    private String description;

    private String bookFile;

    private String coverImage;

    private Genre genre;

    private Level level;

    private Integer downloads;

    private Integer favourites;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
