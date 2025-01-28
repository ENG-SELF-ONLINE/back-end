package ru.engself.readingservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.engself.readingservice.dtos.FavouriteDTO;

import java.util.UUID;

public interface FavouriteService {

    FavouriteDTO addToFavourites(UUID userId, UUID bookId);

    String removeFromFavourites(UUID userId, UUID bookId);

    Page<FavouriteDTO> getAllByUserId(UUID userId, Pageable pageable);

}
