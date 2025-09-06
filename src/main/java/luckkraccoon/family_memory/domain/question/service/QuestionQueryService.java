package luckkraccoon.family_memory.domain.question.service;

import luckkraccoon.family_memory.domain.question.dto.response.QuestionCurrentResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionDetailResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;

public interface QuestionQueryService {
    QuestionListResponse getQuestions(Long chapterId, Long indexId, String q, String sort);
    QuestionDetailResponse getQuestion(Long id);
    QuestionCurrentResponse getCurrentPosition(Long userId, Long chapterId, Long indexId);
    QuestionCurrentResponse getUserQuestionDetail(Long userId, Long chapterId, Long questionId, Long indexId);

}