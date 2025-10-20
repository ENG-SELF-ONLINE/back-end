package ru.engself.authservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.authservice.dtos.UserDTO;
import ru.engself.authservice.enums.Level;

@FeignClient(value = "profile-service", url = "${other.service.url.profile}")
public interface UserFeignController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UserDTO createUser(@RequestPart("userDTO") MultipartFile userDTO, @RequestPart("image") MultipartFile image);

    @GetMapping("/api")
    UserDTO getUserById(@RequestHeader("Authorization") String authorizationHeader);

    @PutMapping("/api")
    UserDTO updateUserById(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/create-progress")
    String createUserProgressForLevel(@RequestParam("level") Level level, @RequestHeader("Authorization") String authorizationHeader);

}

