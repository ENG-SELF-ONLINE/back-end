package ru.engself.dictionaryservice.services;

import ru.engself.dictionaryservice.dtos.FileDTO;
import ru.engself.dictionaryservice.enums.BucketEnum;

import java.util.Optional;

public interface UnsplashService {
    Optional<FileDTO> searchAndSavePhoto(String query, BucketEnum bucket);
}
