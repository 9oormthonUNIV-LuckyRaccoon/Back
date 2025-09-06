package luckkraccoon.family_memory.domain.question.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuestionCurrentResponse {

    private final Long userId;
    private final Long chapterId;
    private final Long indexId;   // null 허용

    private final QuestionSummary question;
    private final AnswerSummary answer; // null 가능
    private final NavSummary nav;

    @Getter
    @Builder
    public static class QuestionSummary {
        private final Long id;
        private final String questionName;
        private final String questionComment;
    }

    @Getter
    @Builder
    public static class AnswerSummary {
        private final Long answerId;
        private final String content;
        private final LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class NavSummary {
        private final Long prevQuestionId; // null 허용
        private final Long nextQuestionId; // null 허용
    }
}