package luckkraccoon.family_memory.domain.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.question.dto.request.AnswerCreateRequest;
import luckkraccoon.family_memory.domain.question.dto.response.AnswerCreateResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionDetailResponse;
import luckkraccoon.family_memory.domain.question.service.QuestionCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;
import luckkraccoon.family_memory.domain.question.service.QuestionQueryService;
import luckkraccoon.family_memory.global.common.response.ApiResponse;
import luckkraccoon.family_memory.global.error.code.status.SuccessStatus;
import luckkraccoon.family_memory.domain.question.service.QuestionService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Question", description = "질문 조회 API")
public class QuestionController {

    private final QuestionQueryService questionQueryService;
    private final QuestionCommandService questionCommandService;

    @Operation(summary = "질문 목록 조회",
            description = "지정한 챕터의 질문 목록을 조회합니다. indexId, q(부분검색), sort(id|questionName,asc|desc) 지원")
    @GetMapping("/chapter/{chapterId}/questions")
    public ApiResponse<QuestionListResponse> getQuestions(@PathVariable Long chapterId,
                                                          @RequestParam(required = false) Long indexId,
                                                          @RequestParam(required = false) String q,
                                                          @RequestParam(required = false, defaultValue = "id,asc") String sort) {
        QuestionListResponse result = questionQueryService.getQuestions(chapterId, indexId, q, sort);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_LIST_SUCCESS, result);
    }

    @Operation(summary = "질문 단건 조회", description = "질문 ID로 단일 질문 상세를 조회합니다.")
    @GetMapping("/questions/{id}")
    public ApiResponse<QuestionDetailResponse> getQuestion(@PathVariable Long id) {
        QuestionDetailResponse result = questionQueryService.getQuestion(id);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_GET_SUCCESS, result);
        // QUESTION_GET_SUCCESS가 없다면: return ApiResponse.onSuccess(SuccessStatus.OK, result);
    }


    @Operation(summary = "질문 답변 등록",
            description = "기존 답변이 있으면 overwrite=true일 때만 덮어씁니다.")
    @PostMapping("/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnswerCreateResponse> createAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerCreateRequest request
    ) {
        AnswerCreateResponse result = questionCommandService.createOrUpdateAnswer(questionId, request);
        // 프로젝트의 SuccessStatus에 맞춰 사용
        return ApiResponse.onSuccess(SuccessStatus.ANSWER_CREATE_SUCCESS, result);
    }
}