package ru.engself.dictionaryservice.services.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.engself.dictionaryservice.dtos.CommonWordDTO;
import ru.engself.dictionaryservice.dtos.responces.DictionaryResponse;
import ru.engself.dictionaryservice.dtos.responces.TranslationResponse;
import ru.engself.dictionaryservice.enums.BucketEnum;
import ru.engself.dictionaryservice.exceptions.GlobalExceptionHandler;
import ru.engself.dictionaryservice.exceptions.TranslationApiException;
import ru.engself.dictionaryservice.mappers.CommonWordMapper;
import ru.engself.dictionaryservice.repositories.CommonWordRepository;
import ru.engself.dictionaryservice.services.CommonWordService;
import ru.engself.dictionaryservice.services.UnsplashService;
import ru.engself.dictionaryservice.utils.feigns.CommonWordFeignController;
import ru.engself.dictionaryservice.utils.feigns.TranslatorFeignClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonWordServiceImpl implements CommonWordService {

    private final CommonWordRepository commonWordRepository;
    private final CommonWordMapper commonWordMapper;
    private final CommonWordFeignController commonWordFeignController;
    private final UnsplashService unsplashService;
    private final TranslatorFeignClient translatorFeignClient;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Value("${translator.rapidapi.host}")
    private String translatorRapidApiHost;

    @Value("${translator.rapidapi.key}")
    private String translatorRapidApiKey;

    @Override
    public CommonWordDTO getOrCreateCommonWord(String word, UUID userId) {
        return commonWordRepository.findByWordName(word.toLowerCase())
                .map(commonWordMapper::toDTO)
                .orElseGet(() -> {
                    CommonWordDTO commonWord = getCommonWordFromApi(word.toLowerCase());
                    if (commonWord != null) {
                        commonWord = commonWordMapper.toDTO(
                                commonWordRepository.save(commonWordMapper.toEntity(commonWord))
                        );
                    }
                    return commonWord;
                });
    }

    @Override
    public String translateWord(String word, String targetLanguage, String sourceLanguage, UUID userId) {
        return getTranslation(word, targetLanguage, sourceLanguage);
    }

    private CommonWordDTO getCommonWordFromApi(String word) {
        List<DictionaryResponse> dictionaryResponseList = commonWordFeignController.getWordDetails(word);

        if (dictionaryResponseList == null || dictionaryResponseList.isEmpty()) {
            throw new RuntimeException("Ошибка при запросе к API словаря");
        }

        DictionaryResponse dictionaryResponse = dictionaryResponseList.get(0);
        String transcription = getTranscription(dictionaryResponse);
        String translatedText = getTranslation(word, "ru", "en");


        return unsplashService.searchAndSavePhoto(word, BucketEnum.WORDS)
                .map(fileDTO ->
                        CommonWordDTO.builder()
                                .wordName(dictionaryResponse.getWord())
                                .wordTranscription(transcription)
                                .wordTranslation(translatedText)
                                .wordPhoto(fileDTO.getFilename())
                                .createdAt(LocalDateTime.now())
                                .build())
                .orElseThrow(() -> new RuntimeException("Ошибка при получении или сохранении фото"));
    }

    private String getTranscription(DictionaryResponse dictionaryResponse) {
        return Optional.ofNullable(dictionaryResponse.getPhonetics())
                .flatMap(phonetics -> phonetics.stream()
                        .filter(p -> p.getText() != null && !p.getText().isEmpty())
                        .findFirst())
                .map(DictionaryResponse.Phonetic::getText)
                .orElse(null);
    }

    private String getTranslation(String word, String targetLanguage, String sourceLanguage) {
        try {
            String requestBody = String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\"}", word, sourceLanguage, targetLanguage);

            TranslationResponse translationResponse = translatorFeignClient.translate(
                    translatorRapidApiKey,
                    translatorRapidApiHost,
                    requestBody
            );

            return Optional.ofNullable(translationResponse)
                    .map(TranslationResponse::getData)
                    .map(TranslationResponse.MyData::getTranslations)
                    .map(TranslationResponse.Translations::getTranslatedText)
                    .orElseThrow(() -> new TranslationApiException("Не удалось получить перевод", null));

        } catch (FeignException e) {
            throw globalExceptionHandler.handleTranslationError(e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка при получении перевода", e);
        }
    }

}