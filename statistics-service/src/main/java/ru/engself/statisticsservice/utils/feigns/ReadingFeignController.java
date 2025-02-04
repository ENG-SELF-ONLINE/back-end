package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "reading-service", url = "${other.service.url.reading}")
public interface ReadingFeignController {

    @GetMapping("/percent")
    Integer getBookProgressPercentByUserId(@RequestHeader("Authorization") String authorizationHeader);

}
