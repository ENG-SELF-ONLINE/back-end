package ru.engself.readingservice.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.FavouriteDTO;
import ru.engself.readingservice.entities.Favourite;
import ru.engself.readingservice.mappers.FavouriteMapper;
import ru.engself.readingservice.repositories.FavouriteRepository;
import ru.engself.readingservice.services.BookService;
import ru.engself.readingservice.services.FavouriteService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final FavouriteMapper favouriteMapper;
    private final BookService bookService;


    @Override
    @Transactional
    public FavouriteDTO addToFavourites(UUID userId, UUID bookId) {

        BookDTO bookDTO = bookService.getBookById(bookId, userId);

        if (favouriteRepository.findByUserIdAndBookBookId(userId, bookId).isPresent()) {
            throw new RuntimeException("This book already in favourites");
        }

        bookService.incrementFavourites(bookId, userId);

        FavouriteDTO favouriteDTO = FavouriteDTO.builder()
                .userId(userId)
                .book(bookDTO)
                .build();

        return favouriteMapper.toDTO(
                favouriteRepository.save(favouriteMapper.toEntity(favouriteDTO))
        );
    }

    @Override
    @Transactional
    public String removeFromFavourites(UUID userId, UUID bookId) {

        Favourite favourite = favouriteRepository.findByUserIdAndBookBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Favourite not found"));

        bookService.decrementFavourites(bookId, userId);

        favouriteRepository.delete(favourite);
        return "successfully deleted";
    }

    @Override
    public Page<FavouriteDTO> getAllByUserId(UUID userId, Pageable pageable) {
        Page<Favourite> favourites = favouriteRepository.findAllByUserId(userId, pageable);
        return favourites.map(favouriteMapper::toDTO);
    }
}
