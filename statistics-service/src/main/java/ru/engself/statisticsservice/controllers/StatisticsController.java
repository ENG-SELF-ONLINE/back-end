package ru.engself.statisticsservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.statisticsservice.dtos.ActivityStatsDTO;
import ru.engself.statisticsservice.dtos.DeckStatisticsDTO;
import ru.engself.statisticsservice.enums.LessonType;
import ru.engself.statisticsservice.services.StatisticsService;

import java.time.LocalDateTime;
import java.util.UUID;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/decks/users/period")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByPeriod(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getStatisticsByPeriod(startDate, endDate, authentication), HttpStatus.OK);
    }

    @GetMapping("/decks/{deckId}")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByDeckId(@PathVariable("deckId") UUID deckId, Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getStatisticsByDeckId(deckId, authentication), HttpStatus.OK);
    }

    @GetMapping("/decks/users")
    public ResponseEntity<DeckStatisticsDTO> getStatisticsByUserId(Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getStatisticsByUserId(authentication), HttpStatus.OK);
    }

    @GetMapping("/book-progress/percent")
    public ResponseEntity<Integer> getBookProgressPercentByUserId(@RequestParam(value = "userId", required = false) UUID userId,
                                                                  Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getBookProgressPercentByUserId(userId, authentication), HttpStatus.OK);
    }

    @GetMapping("/testing-progress/percent")
    public ResponseEntity<Integer> getBookProgressPercentByUserIdAndType(@RequestParam(value = "userId", required = false) UUID userId,
                                                                         LessonType type, Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getBookProgressPercentByUserIdAndType(userId, type, authentication), HttpStatus.OK);
    }

    @GetMapping("/common-progress/percent")
    public ResponseEntity<Integer> getProgressBarByUserId(@RequestParam(value = "userId", required = false) UUID userId,
                                                          Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getProgressBarByUserId(userId, authentication), HttpStatus.OK);
    }

    @GetMapping("/activity")
    public ResponseEntity<ActivityStatsDTO> getActivityStats(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "userId", required = false) UUID userId,
            Authentication authentication) {
        return new ResponseEntity<>(statisticsService.getActivityStats(userId, startDate, endDate, authentication), HttpStatus.OK);
    }

}