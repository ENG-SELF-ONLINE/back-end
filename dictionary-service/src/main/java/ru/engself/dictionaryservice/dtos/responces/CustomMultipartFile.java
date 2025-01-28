package ru.engself.dictionaryservice.dtos.responces;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class CustomMultipartFile implements MultipartFile {

    private final String fileName;
    private final String contentType;
    private final ByteArrayInputStream inputStream;
    private final long fileSize;


    @NotNull
    @Override
    public String getName() {
        return fileName;
    }

    @NotNull
    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @NotNull
    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return fileSize;
    }

    @NotNull
    @Override
    public byte[] getBytes() {
        return inputStream.readAllBytes();
    }

    @NotNull
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Operation not supported");
    }

}
