package ru.engself.testingservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.engself.testingservice.dtos.UserDTO;

import java.util.UUID;

@FeignClient(value = "profile-service", url = "${other.service.url.profile}")
public interface ProfileFeignController {

    @GetMapping("/api")
    UserDTO getUserById(@RequestParam(value = "userId", required = false) UUID friendId, @RequestHeader("Authorization") String authorizationHeader);

}
