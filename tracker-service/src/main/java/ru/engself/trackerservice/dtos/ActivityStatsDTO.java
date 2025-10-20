package ru.engself.trackerservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatsDTO {
    private Map<String, List<ActivityValue>> activities;
    private DeckStatisticsDTO deckStatisticsDTO;
    private double totalTime;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityValue {
        private String date;
        private int value;
    }
}
