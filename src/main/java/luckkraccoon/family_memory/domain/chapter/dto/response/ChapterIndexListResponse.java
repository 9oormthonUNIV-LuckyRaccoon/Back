package luckkraccoon.family_memory.domain.chapter.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChapterIndexListResponse {
    private Long chapterId;
    private String chapterName;
    private int total;
    private List<Item> indexes;

    @Getter
    @Builder
    public static class Item {
        private Long id;
        private String indexName;
        private String indexComment; // null 가능
    }
}