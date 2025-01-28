package ru.engself.dictionaryservice.services;

import ru.engself.dictionaryservice.dtos.CommonWordDTO;

import java.util.UUID;

public interface CommonWordService {

    CommonWordDTO getOrCreateCommonWord(String word, UUID userId);

}
