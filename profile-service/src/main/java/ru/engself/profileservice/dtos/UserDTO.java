package ru.engself.profileservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.profileservice.enums.Level;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
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
