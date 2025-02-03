package ru.engself.trackerservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.engself.trackerservice.dtos.UserDTO;

@FeignClient(value = "profile-service", url = "${other.service.url.profile}")
public interface ProfileFeignController {

    @GetMapping("/api")
    UserDTO getUserById(@RequestHeader("Authorization") String authorizationHeader);

}
