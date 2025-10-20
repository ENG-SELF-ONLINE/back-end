package ru.engself.dictionaryservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeckStatisticsDTO {

    private int totalWords;

    private int newWords;

    private int learningWords;

    private int repeatingWords;

}