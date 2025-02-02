package ru.engself.profileservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.profileservice.enums.FriendshipStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FriendshipDTO {

    private UUID friendshipId;

    private UserDTO sender;

    private UserDTO receiver;

    private FriendshipStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
