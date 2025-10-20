package ru.engself.profileservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.engself.profileservice.entities.Notification;
import ru.engself.profileservice.enums.NotificationType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findNotificationsByRecipientUserId(UUID recipient_userId);

    @Query("SELECT n FROM Notification n WHERE n.recipient.userId = :recipientId AND n.sender.userId = :senderId AND n.type = :notificationType")
    Optional<Notification> findNotificationByRecipientUserIdAndSenderUserIdAndType(UUID recipientId, UUID senderId, NotificationType notificationType);

    @Query("SELECT n FROM Notification n WHERE n.recipient.userId = :recipientId AND n.type = :notificationType")
    Optional<Notification> findNotificationsByRecipientIdAndNotificationType(UUID recipientId, NotificationType notificationType);
}
