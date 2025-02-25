package ru.engself.profileservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.profileservice.dtos.FriendshipDTO;
import ru.engself.profileservice.enums.FriendshipStatus;
import ru.engself.profileservice.services.FriendshipService;

import java.util.Set;
import java.util.UUID;

import static ru.engself.profileservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/{recipientId}")
    public ResponseEntity<FriendshipDTO> sendFriendRequest(@PathVariable UUID recipientId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(friendshipService.sendFriendRequest(userId, recipientId), HttpStatus.OK);
    }

    @PutMapping("/{friendshipId}")
    public ResponseEntity<FriendshipDTO> updateFriendRequestStatus(@PathVariable UUID friendshipId,
                                                                   @RequestParam("status") FriendshipStatus status, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(friendshipService.updateFriendRequestStatus(userId, friendshipId, status), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<FriendshipDTO>> getFriendships(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(friendshipService.getUserFriendships(userId), HttpStatus.OK);
    }

    @GetMapping("/{friendshipId}")
    public ResponseEntity<FriendshipDTO> getFriendshipById(@PathVariable UUID friendshipId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(friendshipService.getFriendshipById(friendshipId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<String> deleteFriendshipById(@PathVariable UUID friendshipId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(friendshipService.deleteFriendshipById(friendshipId, userId), HttpStatus.OK);
    }

}
