package ru.engself.readingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookProgressDTO {

    private UUID bookProgressId;

    private UserDTO userInfo;

    private BookDTO book;

    private Boolean isCompleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
