package ru.engself.photoservice.services;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import ru.engself.photoservice.dtos.FileDTO;
import ru.engself.photoservice.enums.BucketEnum;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileService {
    List<FileDTO> getListObjects(String bucket);

    FileDTO uploadFile(MultipartFile file, String bucket) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    byte[] downloadFile(String filename, String bucket);

    String deleteFile(String file, String bucket);
}