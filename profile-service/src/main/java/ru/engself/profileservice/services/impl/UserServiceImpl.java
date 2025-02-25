package ru.engself.profileservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.enums.BucketEnum;
import ru.engself.profileservice.enums.Level;
import ru.engself.profileservice.exceptions.UserNotFoundException;
import ru.engself.profileservice.mappers.UserMapper;
import ru.engself.profileservice.repositories.UserRepository;
import ru.engself.profileservice.services.UserService;
import ru.engself.profileservice.utils.feigns.PhotoFeignController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.engself.profileservice.enums.Level.C2;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PhotoFeignController photoFeignController;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public UserDTO createUser(MultipartFile userDTO, MultipartFile image) {

        try (InputStream inputStream = userDTO.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            String userDTOString = new String(bytes);

            UserDTO users = objectMapper.readValue(userDTOString, UserDTO.class);
            String photo = photoFeignController.uploadFile(image, BucketEnum.PROFILE).getFilename();

            UserDTO user = UserDTO.builder()
                    .userId(users.getUserId())
                    .email(users.getEmail())
                    .firstName(users.getFirstName())
                    .lastName(users.getLastName())
                    .photo(photo)
                    .level(users.getLevel())
                    .build();

            return userMapper.toDTO(
                    userRepository.save(userMapper.toEntity(user))
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("There is no user with id: " + userId));
    }

    @Override
    public UserDTO updateUserById(UserDTO userDTO, UUID userId) {

        UserDTO user = getUserById(userId);

        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        Level level = userDTO.getLevel();

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName);
        }
        if (level != null) {
            user.setLevel(level);
        }
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toDTO(
                userRepository.save(userMapper.toEntity(user))
        );
    }

    @Override
    public UserDTO getUserByEmail(String email, UUID userId) {
        return userRepository.findUserByEmail(email).map(userMapper::toDTO).orElseThrow(
                () -> new UserNotFoundException("There is no user with email: " + email)
        );
    }

    @Override
    @Transactional
    public UserDTO updateUserPhoto(MultipartFile image, UUID userId) {

        UserDTO currentUser = getUserById(userId);
        String prevPhoto = currentUser.getPhoto();
        String newPhoto = photoFeignController.uploadFile(image, BucketEnum.PROFILE).getFilename();

        currentUser.setPhoto(newPhoto);
        currentUser = userMapper.toDTO(
                userRepository.save(userMapper.toEntity(currentUser))
        );

        if (prevPhoto != null && !prevPhoto.isEmpty())
            photoFeignController.deleteFile(prevPhoto, BucketEnum.PROFILE);

        return currentUser;
    }

    @Override
    public String getUserNextLevel(UUID friendId, UUID userId) {

        if (friendId != null) {
            userId = friendId;
        }

        UserDTO user = getUserById(userId);
        int nextPosition = user.getLevel().ordinal() + 1;

        return !C2.equals(user.getLevel()) ?
                Level.values()[nextPosition].name() : "";
    }

    @Override
    public String deleteUserById(UUID userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("There is no user with id: " + userId);
        }

        userRepository.deleteById(userId);

        return "successful deleted";
    }
}
