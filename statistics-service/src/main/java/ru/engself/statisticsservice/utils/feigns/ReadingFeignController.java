package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "reading-service", url = "${other.service.url.reading}")
public interface ReadingFeignController {

    @GetMapping("/percent")
    Integer getBookProgressPercentByUserId(@RequestParam(value = "userId", required = false) UUID userId,
                                           @RequestHeader("Authorization") String authorizationHeader);

}
