package ru.engself.authservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.authservice.dtos.UserDTO;

@FeignClient(value = "profile-service", url = "${other.service.url.profile}")
public interface UserFeignController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UserDTO createUser(@RequestPart("userDTO") MultipartFile userDTO, @RequestPart("image") MultipartFile image);

}

