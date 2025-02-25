package ru.engself.profileservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.profileservice.dtos.FriendshipDTO;
import ru.engself.profileservice.dtos.NotificationDTO;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.entities.Friendship;
import ru.engself.profileservice.enums.FriendshipStatus;
import ru.engself.profileservice.enums.NotificationType;
import ru.engself.profileservice.exceptions.UserNotFoundException;
import ru.engself.profileservice.mappers.FriendshipMapper;
import ru.engself.profileservice.repositories.FriendshipRepository;
import ru.engself.profileservice.services.FriendshipService;
import ru.engself.profileservice.services.NotificationService;
import ru.engself.profileservice.services.UserService;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final FriendshipMapper friendshipMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public FriendshipDTO sendFriendRequest(UUID userId, UUID recipientId) {

        UserDTO sender = userService.getUserById(userId);
        UserDTO receiver = userService.getUserById(recipientId);

        Optional<Friendship> existingFriendship = friendshipRepository.findExistingFriendship(userId, recipientId);

        if (existingFriendship.isPresent()) {
            throw new IllegalStateException("Friend already exists.");
        }

        FriendshipDTO friendship = FriendshipDTO.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendshipStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        friendship = friendshipMapper.toDTO(
                friendshipRepository.save(friendshipMapper.toEntity(friendship))
        );

        sendFriendRequestNotification(friendship.getFriendshipId(), userId, recipientId);

        return friendship;
    }

    @Override
    public FriendshipDTO updateFriendRequestStatus(UUID userId, UUID friendshipId, FriendshipStatus status) {
        FriendshipDTO friendship = getFriendshipById(friendshipId, userId);
        validateReceiver(userId, friendship);

        NotificationDTO notification = notificationService.getNotificationByUsersIdAndType(
                friendship.getReceiver().getUserId(),
                friendship.getSender().getUserId(),
                NotificationType.FRIEND_REQUEST,
                userId
        );

        return status == FriendshipStatus.ACCEPTED
                ? acceptFriendRequest(userId, friendship, notification)
                : rejectFriendRequest(userId, friendship, notification);
    }

    @Override
    public Set<FriendshipDTO> getUserFriendships(UUID userId) {
        return friendshipRepository.findFriendshipsByUserId(userId).stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED).map(friendshipMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public FriendshipDTO getFriendshipById(UUID friendshipId, UUID userId) {
        return friendshipRepository.findById(friendshipId).map(friendshipMapper::toDTO).orElseThrow(
                () -> new EntityNotFoundException("There is no friendship with id: " + friendshipId)
        );
    }

    @Override
    public String deleteFriendshipById(UUID friendshipId, UUID userId) {
        FriendshipDTO friendship = getFriendshipById(friendshipId, userId);
        validateUserCanDeleteFriendship(friendship, userId);

        friendshipRepository.deleteById(friendshipId);
        return "successful deleted";
    }

    private FriendshipDTO acceptFriendRequest(UUID userId, FriendshipDTO friendship, NotificationDTO notification) {

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        notificationService.deleteNotificationById(notification.getNotificationId(), userId);

        return friendshipMapper.toDTO(
                friendshipRepository.save(friendshipMapper.toEntity(friendship))
        );
    }

    private FriendshipDTO rejectFriendRequest(UUID userId, FriendshipDTO friendship, NotificationDTO notification) {

        friendship.setStatus(FriendshipStatus.REJECTED);
        notificationService.deleteNotificationById(notification.getNotificationId(), userId);

        friendshipRepository.delete(
                friendshipMapper.toEntity(friendship)
        );

        return friendshipMapper.toDTO(friendshipMapper.toEntity(friendship));
    }

    private void sendFriendRequestNotification(UUID contextId, UUID userId, UUID recipientId) {
        NotificationDTO notification = NotificationDTO.builder()
                .message("Вас хотят добавить в друзья!")
                .type(NotificationType.FRIEND_REQUEST)
                .contextId(contextId)
                .build();

        notificationService.createNotification(recipientId, userId, notification, userId);
    }

    private void validateReceiver(UUID userId, FriendshipDTO friendship) {
        if (!friendship.getReceiver().getUserId().equals(userId)) {
            throw new NotFoundException("You are not the recipient of this request!");
        }
    }

    private void validateUserCanDeleteFriendship(FriendshipDTO friendship, UUID userId) {
        if (!(friendship.getReceiver().getUserId().equals(userId) || friendship.getSender().getUserId().equals(userId))) {
            throw new UserNotFoundException("You cannot delete friendship that is not yours!");
        }
    }
}
