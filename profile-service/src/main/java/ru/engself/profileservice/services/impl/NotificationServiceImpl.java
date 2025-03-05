package ru.engself.profileservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.profileservice.dtos.NotificationDTO;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.entities.Notification;
import ru.engself.profileservice.enums.NotificationType;
import ru.engself.profileservice.exceptions.UserNotFoundException;
import ru.engself.profileservice.mappers.NotificationMapper;
import ru.engself.profileservice.repositories.NotificationRepository;
import ru.engself.profileservice.services.NotificationService;
import ru.engself.profileservice.services.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    @Override
    public List<NotificationDTO> getUserNotifications(UUID userId) {
        return notificationRepository.findNotificationsByRecipientUserId(userId).stream()
                .map(notificationMapper::toDTO).toList();
    }

    @Override
    public NotificationDTO createNotification(UUID recipientId, UUID senderId, NotificationDTO notificationDTO, UUID userId) {

        UserDTO sender = userService.getUserById(senderId);
        UserDTO receiver = userService.getUserById(recipientId);

        NotificationDTO notification = NotificationDTO.builder()
                .sender(sender)
                .recipient(receiver)
                .message(notificationDTO.getMessage())
                .type(notificationDTO.getType())
                .contextId(notificationDTO.getContextId())
                .isRead(false)
                .build();

        return notificationMapper.toDTO(
                notificationRepository.save(notificationMapper.toEntity(notification))
        );
    }

    @Override
    public String deleteNotificationById(UUID notificationId, UUID userId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserNotFoundException("There is no notification with id: " + notificationId));

        notificationRepository.delete(notification);
        return "successful deleted";
    }

    @Override
    public NotificationDTO getNotificationByUsersIdAndType(UUID recipientId, UUID senderId, NotificationType notificationType, UUID userId) {

        Notification notification = notificationRepository
                .findNotificationByRecipientUserIdAndSenderUserIdAndType(recipientId, senderId, notificationType)
                .orElseThrow(() -> new UserNotFoundException("There is no notification with the specified parameters"));

        return notificationMapper.toDTO(notification);
    }

    @Override
    public NotificationDTO getNotificationByRecipientIdAndType(UUID recipientId, NotificationType notificationType, UUID userId) {
        Notification notification = notificationRepository
                .findNotificationsByRecipientIdAndNotificationType(recipientId, notificationType)
                .orElseThrow(() -> new UserNotFoundException("There is no notification with the specified parameters"));

        return notificationMapper.toDTO(notification);
    }

    @Override
    public String acceptLevelUpgrade(UUID notificationId, UUID userId) {
        userService.upgradeUserLevel(userId);
        deleteNotificationById(notificationId, userId);

        return "Notification was successfully deleted";
    }
}
