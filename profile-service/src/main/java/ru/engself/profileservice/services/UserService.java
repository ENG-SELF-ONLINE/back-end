package ru.engself.profileservice.services;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.enums.Level;

import java.util.UUID;

public interface UserService {

    UserDTO createUser(MultipartFile userDTO, MultipartFile image);

    UserDTO getUserById(UUID userId);

    UserDTO updateUserById(UserDTO userDTO, UUID userId);

    UserDTO upgradeUserLevel(Authentication authentication);

    String deleteUserById(UUID userId);

    UserDTO getUserByEmail(String email, UUID userId);

    UserDTO updateUserPhoto(MultipartFile image, UUID userId);

    String getUserNextLevel(UUID friendId, UUID userId);

    String createUserProgressForLevel(Level level, Authentication authentication);
}
