package ru.engself.profileservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.enums.Level;
import ru.engself.profileservice.services.UserService;

import java.util.UUID;

import static ru.engself.profileservice.utils.AuthenticationUtils.getUserIdFromAuthentication;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestPart("userDTO") MultipartFile userDTO, @RequestPart("image") MultipartFile image) {
        return new ResponseEntity<>(userService.createUser(userDTO, image), HttpStatus.OK);
    }

    @GetMapping("/api")
    public ResponseEntity<UserDTO> getUserById(@RequestParam(value = "userId", required = false) UUID friendId, Authentication authentication) {
        UUID userId;

        if (friendId != null) {
            userId = friendId;
        } else userId = getUserIdFromAuthentication(authentication);

        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/api/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam("email") String email, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userService.getUserByEmail(email, userId), HttpStatus.OK);
    }

    @GetMapping("/next-level")
    public ResponseEntity<String> getUserNextLevel(@RequestParam(value = "friendId", required = false) UUID friendId, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userService.getUserNextLevel(friendId, userId), HttpStatus.OK);
    }

    @PutMapping("/api")
    public ResponseEntity<UserDTO> updateUserById(@RequestBody UserDTO userDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userService.updateUserById(userDTO, userId), HttpStatus.OK);
    }

    @PostMapping(value = "/api/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> updateUserPhoto(@RequestPart("image") MultipartFile image, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userService.updateUserPhoto(image, userId), HttpStatus.OK);
    }

    @DeleteMapping("/api")
    public ResponseEntity<String> deleteUserById(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }

}