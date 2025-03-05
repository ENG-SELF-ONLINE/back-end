package ru.engself.profileservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.profileservice.dtos.NotificationDTO;
import ru.engself.profileservice.enums.NotificationType;
import ru.engself.profileservice.services.NotificationService;

import java.util.List;
import java.util.UUID;

import static ru.engself.profileservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.getUserNotifications(userId), HttpStatus.OK);
    }

    @GetMapping("/recipient/{recipientId}/sender/{senderId}")
    public ResponseEntity<NotificationDTO> getNotificationByUsersIdAndType(@PathVariable UUID recipientId, @PathVariable UUID senderId,
                                                                           NotificationType notificationType, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.getNotificationByUsersIdAndType(recipientId, senderId, notificationType, userId), HttpStatus.OK);
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<NotificationDTO> getNotificationByRecipientIdAndType(@PathVariable UUID recipientId,
                                                                               @RequestParam("type") NotificationType notificationType, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.getNotificationByRecipientIdAndType(recipientId, notificationType, userId), HttpStatus.OK);
    }

    @PostMapping("/recipient/{recipientId}/sender/{senderId}")
    public ResponseEntity<NotificationDTO> createNotification(@PathVariable UUID recipientId, @PathVariable UUID senderId,
                                                              @RequestBody NotificationDTO notificationDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.createNotification(recipientId, senderId, notificationDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable UUID notificationId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.deleteNotificationById(notificationId, userId), HttpStatus.OK);
    }

    @PostMapping("/{notificationId}/accept")
    public ResponseEntity<String> acceptLevelUpgrade(@PathVariable UUID notificationId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(notificationService.acceptLevelUpgrade(notificationId, userId), HttpStatus.OK);
    }

}
