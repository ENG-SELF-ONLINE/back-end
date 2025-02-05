package ru.engself.statisticsservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.engself.statisticsservice.enums.LessonType;

@FeignClient(name = "testing-service", url = "${other.service.url.testing}")
public interface TestingFeignController {

    @GetMapping("percent")
    Integer getBookProgressPercentByUserIdAndType(@RequestParam("type") LessonType type, @RequestHeader("Authorization") String authorizationHeader);

}
