package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.engself.statisticsservice.dtos.NotificationDTO;
import ru.engself.statisticsservice.dtos.UserDTO;
import ru.engself.statisticsservice.enums.NotificationType;

import java.util.UUID;

@FeignClient(value = "profile-service", url = "${other.service.url.profile}")
public interface ProfileFeignController {

    @GetMapping("/users/api")
    UserDTO getUserById(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/notifications/recipient/{recipientId}")
    NotificationDTO getNotificationByRecipientIdAndType(@PathVariable UUID recipientId,
                                                        @RequestBody NotificationType notificationType,
                                                        @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("notifications/recipient/{recipientId}/sender/{senderId}")
    NotificationDTO createNotification(@PathVariable UUID recipientId,
                                       @PathVariable UUID senderId,
                                       @RequestBody NotificationDTO notificationDTO,
                                       @RequestHeader("Authorization") String authorizationHeader);
}
