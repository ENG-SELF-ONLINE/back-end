package ru.engself.readingservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.readingservice.dtos.FileDTO;
import ru.engself.readingservice.enums.BucketEnum;

@FeignClient(value = "photo-service", url = "${other.service.url.photo}")
public interface PhotoFeignController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileDTO uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("bucket") BucketEnum bucket);

    @GetMapping(path = "/pdf/download")
    ByteArrayResource downloadPdf(@RequestParam(value = "file") String file, @RequestParam("bucket") BucketEnum bucket);

}
