package ru.engself.dictionaryservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.dictionaryservice.dtos.FileDTO;
import ru.engself.dictionaryservice.enums.BucketEnum;

@FeignClient(name = "file-service", url = "${other.service.url.photo}")
public interface PhotoFeignController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDTO uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("bucket") BucketEnum bucket);

}
