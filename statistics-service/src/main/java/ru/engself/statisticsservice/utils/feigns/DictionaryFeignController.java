package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.engself.statisticsservice.dtos.DeckStatisticsDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@FeignClient(value = "dictionary-service", url = "${other.service.url.dictionary}")
public interface DictionaryFeignController {

    @GetMapping("/decks/{deckId}/statistics")
    DeckStatisticsDTO getStatisticsByDeckId(@PathVariable("deckId") UUID deckId, @RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/users/statistics")
    DeckStatisticsDTO getStatisticsByUserId(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/users/statistics/period")
    DeckStatisticsDTO getStatisticsByPeriod(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestHeader("Authorization") String authorizationHeader);

}
