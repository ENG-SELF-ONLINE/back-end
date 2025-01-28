package ru.engself.dictionaryservice.utils.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.engself.dictionaryservice.dtos.responces.DictionaryResponse;

import java.util.List;

@FeignClient(value = "common-word-service", url = "${other.service.url.common-word}")
public interface CommonWordFeignController {

    @GetMapping("/{word}")
    List<DictionaryResponse> getWordDetails(@PathVariable("word") String word);

}
