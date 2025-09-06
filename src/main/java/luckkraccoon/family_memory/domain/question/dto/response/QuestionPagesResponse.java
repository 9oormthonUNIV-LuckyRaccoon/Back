package luckkraccoon.family_memory.domain.question.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class QuestionPagesResponse {
    private Long userId;
    private Long chapterId;
    private Long indexId;   // null 가능
    private int  size;
    private List<PageItem> items;
    private PageNav pageNav;

    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class PageItem {
        private QuestionDto question;
        private AnswerDto   answer;   // null 가능
    }

    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class QuestionDto {
        private Long id;
        private String questionName;
        private String questionComment;
    }

    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class AnswerDto {
        private Long answerId;
        private Long userId;
        private Long questionId;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class PageNav {
        private Long prevAnchorQuestionId; // null 가능
        private Long nextAnchorQuestionId; // null 가능
    }
}