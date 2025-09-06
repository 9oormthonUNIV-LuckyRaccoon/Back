package luckkraccoon.family_memory.domain.question.converter;

import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.question.dto.response.*;
import luckkraccoon.family_memory.domain.question.entity.Question;
import luckkraccoon.family_memory.domain.question.entity.UserQuestion;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public static QuestionDetailResponse toDetail(Question q) {
        Long indexId = (q.getIndex() != null) ? q.getIndex().getId() : null;
        Long chapterId = (q.getIndex() != null && q.getIndex().getChapter() != null)
                ? q.getIndex().getChapter().getId()
                : null;

        return QuestionDetailResponse.builder()
                .id(q.getId())
                .questionName(q.getQuestionName())
                .questionComment(q.getQuestionComment())
                .chapterId(chapterId)
                .indexId(indexId)
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }

    public static AnswerCreateResponse toAnswerCreateResponse(UserQuestion saved, UserChapter uc) {
        return AnswerCreateResponse.builder()
                .answerId(saved.getId())
                .questionId(saved.getQuestion().getId())
                .userId(saved.getUser().getId())
                .content(saved.getAnswer())             // <-- answer 사용
                .createdAt(saved.getCreatedAt())
                .userChapter(
                        AnswerCreateResponse.UserChapterSummary.builder()
                                .userChapterId(uc.getId())
                                .state(uc.getState() != null ? uc.getState().name() : null)
                                .progressPercent(uc.getProgressPercent())
                                .lastQuestionId(uc.getLastQuestion() != null ? uc.getLastQuestion().getId() : null)
                                .finishedAt(uc.getFinishedAt())
                                .build()
                )
                .build();
    }

    public static QuestionCurrentResponse toCurrentResponse(
            Long userId, Long chapterId, Long indexId,
            Question question,
            UserQuestion userQuestion,
            Long prevId, Long nextId
    ) {
        return QuestionCurrentResponse.builder()
                .userId(userId)
                .chapterId(chapterId)
                .indexId(indexId)
                .question(
                        QuestionCurrentResponse.QuestionSummary.builder()
                                .id(question.getId())
                                .questionName(question.getQuestionName())
                                .questionComment(question.getQuestionComment())
                                .build()
                )
                .answer(userQuestion == null ? null :
                        QuestionCurrentResponse.AnswerSummary.builder()
                                .answerId(userQuestion.getId())
                                .content(userQuestion.getAnswer())
                                .createdAt(userQuestion.getCreatedAt())
                                .build()
                )
                .nav(
                        QuestionCurrentResponse.NavSummary.builder()
                                .prevQuestionId(prevId)
                                .nextQuestionId(nextId)
                                .build()
                )
                .build();
    }

    public static QuestionPagesResponse toPagesResponse(
            Long userId, Long chapterId, Long indexId, int size,
            List<Question> questions, Map<Long, UserQuestion> answerMap,
            Long prevAnchorId, Long nextAnchorId
    ) {
        List<QuestionPagesResponse.PageItem> items = questions.stream()
                .map(q -> QuestionPagesResponse.PageItem.builder()
                        .question(QuestionPagesResponse.QuestionDto.builder()
                                .id(q.getId())
                                .questionName(q.getQuestionName())
                                .questionComment(q.getQuestionComment())
                                .build())
                        .answer(Optional.ofNullable(answerMap.get(q.getId()))
                                .map(a -> QuestionPagesResponse.AnswerDto.builder()
                                        .answerId(a.getId())
                                        .userId(a.getUser().getId())
                                        .questionId(a.getQuestion().getId())
                                        .content(a.getAnswer())
                                        .createdAt(a.getCreatedAt())
                                        .build())
                                .orElse(null))
                        .build())
                .toList();

        return QuestionPagesResponse.builder()
                .userId(userId)
                .chapterId(chapterId)
                .indexId(indexId)
                .size(items.size())
                .items(items)
                .pageNav(QuestionPagesResponse.PageNav.builder()
                        .prevAnchorQuestionId(prevAnchorId)
                        .nextAnchorQuestionId(nextAnchorId)
                        .build())
                .build();
    }

}