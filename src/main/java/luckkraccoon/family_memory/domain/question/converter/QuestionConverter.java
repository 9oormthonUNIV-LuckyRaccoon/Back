package luckkraccoon.family_memory.domain.question.converter;

import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;
import luckkraccoon.family_memory.domain.question.entity.Question;

import java.util.List;

public class QuestionConverter {

    public static QuestionListResponse toListResponse(Long chapterId,
                                                      Long indexId,
                                                      List<Question> questions) {

        List<QuestionListResponse.QuestionItem> items = questions.stream()
                .map(q -> QuestionListResponse.QuestionItem.builder()
                        .id(q.getId())
                        .questionName(q.getQuestionName())
                        .questionComment(q.getQuestionComment())
                        .indexId(q.getIndex() != null ? q.getIndex().getId() : null)
                        .build())
                .toList();

        return QuestionListResponse.builder()
                .chapterId(chapterId)
                .indexId(indexId)
                .total(items.size())
                .questions(items)
                .build();
    }
}