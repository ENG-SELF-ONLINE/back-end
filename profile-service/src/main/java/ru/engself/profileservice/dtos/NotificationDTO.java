package ru.engself.profileservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.engself.profileservice.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NotificationDTO {

    private UUID notificationId;

    private UserDTO recipient;

    private UserDTO sender;

    private UUID contextId;

    private NotificationType type;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
