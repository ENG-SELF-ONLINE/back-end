package ru.engself.dictionaryservice.dtos.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {

    @JsonProperty("results")
    private List<Photo> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo {

        @JsonProperty("id")
        private String id;

        @JsonProperty("urls")
        private Urls urls;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Urls {

        @JsonProperty("regular")
        private String regular;

    }

}
