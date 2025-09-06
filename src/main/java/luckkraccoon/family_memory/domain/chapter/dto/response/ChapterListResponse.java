package luckkraccoon.family_memory.domain.chapter.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChapterListResponse {
    private int size;           // 실제 반환 개수(최대 6)
    private String sort;        // 사용된 정렬 (예: "createdAt,asc")
    private List<Item> chapters;

    @Getter
    @Builder
    public static class Item {
        private Long id;
        private String chapterName;
        private String chapterComment;
        private String createdAt;   // ISO-8601
        private String updatedAt;   // ISO-8601
    }
}