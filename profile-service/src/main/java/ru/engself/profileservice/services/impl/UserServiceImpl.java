package ru.engself.profileservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.profileservice.dtos.BookDTO;
import ru.engself.profileservice.dtos.LessonDTO;
import ru.engself.profileservice.dtos.UserDTO;
import ru.engself.profileservice.dtos.UserTestResultDTO;
import ru.engself.profileservice.enums.BucketEnum;
import ru.engself.profileservice.enums.LessonType;
import ru.engself.profileservice.enums.Level;
import ru.engself.profileservice.exceptions.UserNotFoundException;
import ru.engself.profileservice.mappers.UserMapper;
import ru.engself.profileservice.repositories.UserRepository;
import ru.engself.profileservice.services.UserService;
import ru.engself.profileservice.utils.feigns.PhotoFeignController;
import ru.engself.profileservice.utils.feigns.ReadingFeignController;
import ru.engself.profileservice.utils.feigns.TestingFeignController;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.engself.profileservice.enums.Level.C2;
import static ru.engself.profileservice.utils.AuthenticationUtils.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PhotoFeignController photoFeignController;
    private final ReadingFeignController readingFeignController;
    private final TestingFeignController testingFeignController;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

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
        boolean isInit = userDTO.isInitialized();

        if (firstName != null && !firstName.isBlank()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            user.setLastName(lastName);
        }
        if (level != null) {
            user.setLevel(level);
        }
        if (isInit) {
            user.setInitialized(true);
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
    @Transactional
    public UserDTO upgradeUserLevel(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        UserDTO user = getUserById(userId);

        Level nextLevel = Level.valueOf(getUserNextLevel(null, userId));
        user.setLevel(nextLevel);

        createUserProgressForLevel(nextLevel, authentication);

        kafkaTemplate.send("reading-updates", generateKeyPrefix("book_progress", userId));
        kafkaTemplate.send("testing-updates", generateKeyPrefix("test_progress_type", userId));

        return userMapper.toDTO(
                userRepository.save(userMapper.toEntity(user))
        );
    }

    @Override
    public String deleteUserById(UUID userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("There is no user with id: " + userId);
        }

        userRepository.deleteById(userId);

        return "successful deleted";
    }

    private void createBookProgressIfNotExists(BookDTO book, String authorizationHeader) {
        try {
            readingFeignController.createBookProgress(book, authorizationHeader);
        } catch (FeignException e) {
            throw new RuntimeException("Ошибка при вызове Feign-клиента для создания прогресса книги: " + e.getMessage());
        }
    }

    private void createUserTestResultIfNotExists(LessonDTO lesson, String authorizationHeader) {
        try {
            UserTestResultDTO userTestResultDTO = UserTestResultDTO.builder()
                    .score(0)
                    .passed(false)
                    .build();

            testingFeignController.createUserTestResult(userTestResultDTO, lesson.getLessonId(), authorizationHeader);
        } catch (FeignException e) {
            throw new RuntimeException("Ошибка при вызове Feign-клиента для создания результата теста: " + e.getMessage());
        }
    }

    @Override
    public String createUserProgressForLevel(Level level, Authentication authentication) {
        List<LessonDTO> grammarLessons = testingFeignController.getLessonsByLessonTypeAndLevel(level, LessonType.GRAMMAR, getAuthorizationHeader(authentication));
        List<LessonDTO> listeningLessons = testingFeignController.getLessonsByLessonTypeAndLevel(level, LessonType.LISTENING, getAuthorizationHeader(authentication));
        List<BookDTO> books = readingFeignController.getAllByLevel(level, getAuthorizationHeader(authentication));

        for (BookDTO book : books) {
            createBookProgressIfNotExists(book, getAuthorizationHeader(authentication));
        }

        for (LessonDTO grammarLesson : grammarLessons) {
            createUserTestResultIfNotExists(grammarLesson, getAuthorizationHeader(authentication));
        }

        for (LessonDTO listeningLesson : listeningLessons) {
            createUserTestResultIfNotExists(listeningLesson, getAuthorizationHeader(authentication));
        }

        return "successfully updated";
    }
}
