package ru.engself.photoservice.controllers;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.photoservice.dtos.FileDTO;
import ru.engself.photoservice.enums.BucketEnum;
import ru.engself.photoservice.services.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileDTO>> getFiles(BucketEnum bucket) {
        return new ResponseEntity<>(fileService.getListObjects(bucket.getBucketName()), HttpStatus.OK);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestPart("file") MultipartFile file, BucketEnum bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok().body(fileService.uploadFile(file, bucket.getBucketName()));
    }

    @GetMapping(path = "/images/download")
    public ResponseEntity<ByteArrayResource> downloadImage(@RequestParam(value = "file") String file, BucketEnum bucket) {
        return downloadFile(file, bucket.getBucketName(), "image/jpeg", "attachment");
    }

    @GetMapping(path = "/images/show")
    public ResponseEntity<ByteArrayResource> showImage(@RequestParam(value = "file") String file, BucketEnum bucket) {
        return downloadFile(file, bucket.getBucketName(), "image/jpeg", "inline");
    }

    @GetMapping(path = "/audios/show")
    public ResponseEntity<ByteArrayResource> showAudio(@RequestParam(value = "file") String file, BucketEnum bucket) {
        return downloadFile(file, bucket.getBucketName(), "audio/mpeg", "inline");
    }

    @GetMapping(path = "/pdf/download")
    public ResponseEntity<ByteArrayResource> downloadPdf(@RequestParam(value = "file") String file, BucketEnum bucket) {
        return downloadFile(file, bucket.getBucketName(), "application/pdf", "attachment");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFile(@RequestParam("file") String file, BucketEnum bucket) {
        return new ResponseEntity<>(fileService.deleteFile(file, bucket.getBucketName()), HttpStatus.OK);
    }

    private ResponseEntity<ByteArrayResource> downloadFile(String file, String bucket, String contentType, String contentDisposition) {
        byte[] data = fileService.downloadFile(file, bucket);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", contentType)
                .header("Content-disposition", contentDisposition + "; filename=\"" + file + "\"")
                .body(resource);
    }

}