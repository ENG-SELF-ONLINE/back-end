package ru.engself.statisticsservice.dtos;

import lombok.*;
import ru.engself.statisticsservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID userId;

    private String email;

    private String firstName;

    private String lastName;

    private String photo;

    private Level level;

    private boolean initialized;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
