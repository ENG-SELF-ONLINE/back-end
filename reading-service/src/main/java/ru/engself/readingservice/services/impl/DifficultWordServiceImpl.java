package ru.engself.readingservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.engself.readingservice.dtos.BookDTO;
import ru.engself.readingservice.dtos.DifficultWordDTO;
import ru.engself.readingservice.entities.DifficultWord;
import ru.engself.readingservice.mappers.DifficultWordMapper;
import ru.engself.readingservice.repositories.DifficultWordRepository;
import ru.engself.readingservice.services.DifficultWordService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DifficultWordServiceImpl implements DifficultWordService {

    private final DifficultWordRepository difficultWordRepository;
    private final DifficultWordMapper difficultWordMapper;

    @Override
    @Transactional
    public List<DifficultWordDTO> updateDifficultWords(List<DifficultWordDTO> difficultWordDTOs, BookDTO book) {
        if (difficultWordDTOs == null) {
            return Collections.emptyList();
        }

        List<DifficultWord> updatedDifficultWords = new ArrayList<>();
        for (DifficultWordDTO difficultWordDTO : difficultWordDTOs) {
            DifficultWordDTO difficultWord = difficultWordMapper.toDTO((difficultWordDTO.getWordId() != null) ?
                    difficultWordRepository.findById(difficultWordDTO.getWordId())
                            .orElseThrow(() -> new EntityNotFoundException("Word not found")) : new DifficultWord());

            difficultWord.setWord(difficultWordDTO.getWord());
            difficultWord.setBook(book);
            updatedDifficultWords.add(difficultWordRepository.save(difficultWordMapper.toEntity(difficultWord)));
        }

        return updatedDifficultWords.stream()
                .map(difficultWordMapper::toDTO)
                .collect(Collectors.toList());
    }


}
