package ru.engself.readingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FavouriteDTO {

    private UUID favouriteId;

    private UUID userId;

    private BookDTO book;

}
