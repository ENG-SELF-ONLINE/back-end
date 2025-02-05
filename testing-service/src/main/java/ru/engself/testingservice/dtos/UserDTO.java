package ru.engself.testingservice.dtos;

import lombok.*;
import ru.engself.testingservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserDTO {

    private UUID userId;

    private String email;

    private String firstName;

    private String lastName;

    private String photo;

    private Level level;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
