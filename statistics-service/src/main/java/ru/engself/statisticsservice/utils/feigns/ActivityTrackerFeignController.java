package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.engself.statisticsservice.dtos.ActivityStatsDTO;

import java.time.LocalDateTime;

@FeignClient(value = "tracker-service", url = "${other.service.url.tracker}")
public interface ActivityTrackerFeignController {

    @GetMapping("/stats")
    ActivityStatsDTO getActivityStats(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestHeader("Authorization") String authorizationHeader);

}
