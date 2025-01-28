package ru.engself.dictionaryservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.engself.dictionaryservice.dtos.responces.TranslationResponse;

@FeignClient(name = "deep-translator", url = "${translator.rapidapi.url}")
public interface TranslatorFeignClient {

    @PostMapping(value = "/language/translate/v2", consumes = "application/json")
    TranslationResponse translate(
            @RequestHeader("x-rapidapi-key") String rapidApiKey,
            @RequestHeader("x-rapidapi-host") String rapidApiHost,
            @RequestBody String requestBody
    );
}
