package ru.engself.profileservice.services;

import ru.engself.profileservice.dtos.FriendshipDTO;
import ru.engself.profileservice.enums.FriendshipStatus;

import java.util.List;
import java.util.UUID;

public interface FriendshipService {
    FriendshipDTO sendFriendRequest(UUID userId, UUID recipientId);

    FriendshipDTO updateFriendRequestStatus(UUID userId, UUID friendshipId, FriendshipStatus status);

    List<FriendshipDTO> getUserFriendships(UUID userId);

    FriendshipDTO getFriendshipById(UUID friendshipId, UUID userId);

    String deleteFriendshipById(UUID friendshipId, UUID userId);
}
