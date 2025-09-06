package luckkraccoon.family_memory.domain.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionListResponse {

    private Long chapterId;
    private Long indexId;   // null 가능
    private int  total;
    private List<QuestionItem> questions;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionItem {
        private Long   id;
        private String questionName;
        private String questionComment; // null 가능
        private Long   indexId;         // null 가능
    }
}