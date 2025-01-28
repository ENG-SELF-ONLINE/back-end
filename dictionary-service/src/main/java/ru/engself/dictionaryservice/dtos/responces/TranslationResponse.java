package ru.engself.dictionaryservice.dtos.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationResponse {

    @JsonProperty("data")
    private MyData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MyData {

        @JsonProperty("translations")
        private Translations translations;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Translations {

        @JsonProperty("translatedText")
        private String translatedText;
    }
}
