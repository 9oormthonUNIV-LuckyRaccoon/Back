package luckkraccoon.family_memory.domain.question.service;

import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;

public interface QuestionQueryService {
    QuestionListResponse getQuestions(Long chapterId, Long indexId, String q, String sort);

}