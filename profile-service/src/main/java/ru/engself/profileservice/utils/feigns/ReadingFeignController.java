package ru.engself.profileservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.engself.profileservice.dtos.BookDTO;
import ru.engself.profileservice.dtos.BookProgressDTO;
import ru.engself.profileservice.enums.Level;

import java.util.List;

@FeignClient(name = "reading-service", url = "${other.service.url.reading}")
public interface ReadingFeignController {

    @GetMapping("/books/without-page")
    List<BookDTO> getAllByLevel(@RequestParam("level") Level level,
                                @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/book-progress")
    BookProgressDTO createBookProgress(@RequestBody BookDTO bookDTO,
                                       @RequestHeader("Authorization") String authorizationHeader);
}
