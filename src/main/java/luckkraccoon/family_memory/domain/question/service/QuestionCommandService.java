package luckkraccoon.family_memory.domain.question.service;

import luckkraccoon.family_memory.domain.question.dto.request.AnswerCreateRequest;
import luckkraccoon.family_memory.domain.question.dto.response.AnswerCreateResponse;

public interface QuestionCommandService {
    AnswerCreateResponse createOrUpdateAnswer(Long questionId, AnswerCreateRequest request);

}