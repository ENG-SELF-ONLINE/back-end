package ru.engself.readingservice.services;

import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.DifficultWordDTO;

import java.util.List;

public interface DifficultWordService {

    List<DifficultWordDTO> updateDifficultWords(List<DifficultWordDTO> difficultWordDTOs, BookDTO book);

}
