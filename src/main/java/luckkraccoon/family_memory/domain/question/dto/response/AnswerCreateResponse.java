package luckkraccoon.family_memory.domain.question.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerCreateResponse {
    private Long answerId;
    private Long questionId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    private UserChapterSummary userChapter;

    @Getter
    @Builder
    public static class UserChapterSummary {
        private Long userChapterId;
        private String state;          // NONE / PROGRESS / SUCCESS
        private Integer progressPercent;
        private Long lastQuestionId;   // lastQuestion의 PK
        private LocalDateTime finishedAt;
    }
}