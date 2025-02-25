package ru.engself.trackerservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.engself.trackerservice.dtos.ActivityStatsDTO;
import ru.engself.trackerservice.dtos.ActivityTrackerDTO;
import ru.engself.trackerservice.services.ActivityTrackerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.engself.trackerservice.utils.AuthenticationUtils.getUserIdFromAuthentication;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/trackers")
public class ActivityTrackerController {

    private final ActivityTrackerService activityTrackerService;

    @PostMapping
    public ResponseEntity<ActivityTrackerDTO> createActivity(@RequestBody ActivityTrackerDTO activityDTO,
                                                             Authentication authentication) {
        return new ResponseEntity<>(activityTrackerService.createActivity(activityDTO, authentication), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityTrackerDTO> getActivityById(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(activityTrackerService.getActivityById(id, userId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityTrackerDTO> updateActivityById(@PathVariable UUID id, @RequestBody ActivityTrackerDTO activityDTO, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(activityTrackerService.updateActivityById(activityDTO, id, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteActivityById(@PathVariable UUID id, Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(activityTrackerService.deleteActivityById(id, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ActivityTrackerDTO>> getAllActivities(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        return new ResponseEntity<>(activityTrackerService.getAllActivities(startDate, endDate, userId), HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<ActivityStatsDTO> getActivityStats(
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Authentication authentication) {
        return new ResponseEntity<>(activityTrackerService.getActivityStats(userId, startDate, endDate, authentication), HttpStatus.OK);
    }

}
