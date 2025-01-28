package ru.engself.dictionaryservice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.engself.dictionaryservice.dtos.responces.CustomMultipartFile;
import ru.engself.dictionaryservice.dtos.FileDTO;
import ru.engself.dictionaryservice.dtos.responces.SearchResponse;
import ru.engself.dictionaryservice.enums.BucketEnum;
import ru.engself.dictionaryservice.services.UnsplashService;
import ru.engself.dictionaryservice.utils.feigns.PhotoFeignController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnsplashServiceImpl implements UnsplashService {

    @Value("${unsplash.access-key}")
    private String accessKey;

    @Value("${unsplash.search-url}")
    private String unsplashSearchUrl;

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final PhotoFeignController photoFeignController;

    @Override
    public Optional<FileDTO> searchAndSavePhoto(String query, BucketEnum bucket) {
        String url = unsplashSearchUrl + query + "&client_id=" + accessKey;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new RuntimeException("Failed to get image from Unsplash API");
            }

            SearchResponse searchResponse = objectMapper.readValue(Objects.requireNonNull(response.body()).string(), SearchResponse.class);
            return searchResponse.getResults().stream()
                    .findFirst()
                    .flatMap(photo -> downloadAndUploadImage(photo.getUrls().getRegular(), bucket));
        } catch (IOException e) {
            throw new RuntimeException("Error communicating with Unsplash API: " + e.getMessage());
        }
    }

    private Optional<FileDTO> downloadAndUploadImage(String imageUrl, BucketEnum bucket) {
        try {
            byte[] imageBytes = downloadImage(imageUrl);
            String fileName = generateRandomFileName();
            CustomMultipartFile multipartFile = new CustomMultipartFile(fileName, "image/jpeg", new ByteArrayInputStream(imageBytes), imageBytes.length);
            return Optional.of(photoFeignController.uploadFile(multipartFile, bucket));
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading and uploading the photo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while creating multipart file:" + e.getMessage());
        }
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        Request request = new Request.Builder().url(imageUrl).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to download image: " + imageUrl);
            }
            try (InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputStream.toByteArray();
            }
        }
    }

    private String generateRandomFileName() {
        return java.util.UUID.randomUUID() + ".jpg";
    }
}