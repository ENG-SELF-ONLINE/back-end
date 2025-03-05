package ru.engself.profileservice.services;

import ru.engself.profileservice.dtos.NotificationDTO;
import ru.engself.profileservice.enums.NotificationType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDTO> getUserNotifications(UUID userId);

    NotificationDTO createNotification(UUID recipientId, UUID senderId, NotificationDTO notificationDTO, UUID userId);

    String deleteNotificationById(UUID notificationId, UUID userId);

    NotificationDTO getNotificationByUsersIdAndType(UUID recipientId, UUID senderId, NotificationType notificationType, UUID userId);

    NotificationDTO getNotificationByRecipientIdAndType(UUID recipientId, NotificationType notificationType, UUID userId);

    String acceptLevelUpgrade(UUID notificationId, UUID userId);
}
