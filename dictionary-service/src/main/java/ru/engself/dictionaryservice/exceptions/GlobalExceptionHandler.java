package ru.engself.dictionaryservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.engself.dictionaryservice.dtos.responces.TranslationErrorResponse;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = {DeckNotFoundException.class})
    public ResponseEntity<Object> handleProjectNotFoundException(DeckNotFoundException deckNotFoundException) {

        GlobalException taskException = new GlobalException(
                deckNotFoundException.getMessage(),
                deckNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(taskException, taskException.getHttpStatus());
    }

    public TranslationApiException handleTranslationError(FeignException e) {
        String errorMessage = "Ошибка при запросе к API перевода";
        if (e.contentUTF8() != null && !e.contentUTF8().isEmpty()) {
            try {
                TranslationErrorResponse errorResponse = objectMapper.readValue(e.contentUTF8(), TranslationErrorResponse.class);
                if (errorResponse != null && errorResponse.getError() != null) {
                    errorMessage = String.format("Ошибка %d: %s",
                            errorResponse.getError().getCode(),
                            errorResponse.getError().getMessage()
                    );
                }
            }
            catch (JsonProcessingException ex){
                errorMessage = "Не удалось разобрать ответ ошибки: " + e.contentUTF8();
            }
        }
        return new TranslationApiException(errorMessage, e);
    }

}
