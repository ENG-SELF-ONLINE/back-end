package ru.engself.testingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GrammarContentDTO {

    private List<ContentItem> content;
    private List<Source> sources;
    private List<String> youtubeLinks;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class ContentItem {
        private String type;
        private String text;
        private String url;
        private List<String> items;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Source {
        private String type;
        private String url;
        private String name;
    }

}
