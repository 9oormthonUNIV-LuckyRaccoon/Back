package luckkraccoon.family_memory.domain.chapter.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserChaptersResponse {
    private Long userId;
    private String state; // PROGRESS / SUCCESS / ALL
    private List<UserChapterItem> items;

    @Getter
    @Builder
    public static class UserChapterItem {
        private Long userChapterId;
        private String state;            // PROGRESS / SUCCESS / NONE
        private Integer progressPercent; // 0~100 (없으면 0)
        private Long lastQuestionId;     // null 가능
        private String finishedAt;       // ISO-8601 / null
        private String updatedAt;        // ISO-8601
        private ChapterSummary chapter;
    }

    @Getter
    @Builder
    public static class ChapterSummary {
        private Long id;
        private String chapterName;
        private String chapterComment;
    }
}