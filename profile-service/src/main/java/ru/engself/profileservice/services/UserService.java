package ru.engself.profileservice.services;

import org.springframework.web.multipart.MultipartFile;
import ru.engself.profileservice.dtos.UserDTO;

import java.util.UUID;

public interface UserService {

    UserDTO createUser(MultipartFile userDTO, MultipartFile image);

    UserDTO getUserById(UUID userId);

    UserDTO updateUserById(UserDTO userDTO, UUID userId);

    String deleteUserById(UUID userId);

    UserDTO getUserByEmail(String email, UUID userId);

    UserDTO updateUserPhoto(MultipartFile image, UUID userId);
}
