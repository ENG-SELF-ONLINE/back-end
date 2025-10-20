package ru.engself.dictionaryservice.dtos.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictionaryResponse {

    @JsonProperty("word")
    private String word;

    @JsonProperty("phonetic")
    private String phonetic;

    @JsonProperty("phonetics")
    private List<Phonetic> phonetics;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Phonetic {

        @JsonProperty("text")
        private String text;

    }
}