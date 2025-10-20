package ru.engself.statisticsservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeckStatisticsDTO {

    private int totalWords;

    private int newWords;

    private int learningWords;

    private int repeatingWords;

}