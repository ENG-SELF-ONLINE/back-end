package ru.engself.dictionaryservice.dtos.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationErrorResponse {

    @JsonProperty("error")
    private ErrorDetails error;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDetails {

        @JsonProperty("code")
        private int code;

        @JsonProperty("message")
        private String message;

    }


}
