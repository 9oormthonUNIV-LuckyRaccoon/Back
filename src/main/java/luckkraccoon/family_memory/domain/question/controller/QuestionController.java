package luckkraccoon.family_memory.domain.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.question.dto.request.AnswerCreateRequest;
import luckkraccoon.family_memory.domain.question.dto.response.*;
import luckkraccoon.family_memory.domain.question.service.QuestionCommandService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "사용자 자서전 현재 위치 조회",
            description = "lastQuestionId가 있으면 그 질문을, 없으면 범위 내 첫 질문을 반환합니다. prev/next id 포함.")
    @GetMapping("/users/{userId}/chapters/{chapterId}/current")
    public ApiResponse<QuestionCurrentResponse> getCurrent(
            @PathVariable Long userId,
            @PathVariable Long chapterId,
            @RequestParam(name = "indexId", required = false) Long indexId
    ) {
        var result = questionQueryService.getCurrentPosition(userId, chapterId, indexId);
        // 프로젝트에 맞춰 SuccessStatus를 사용하세요. (공용 OK가 없다면 QUESTION_GET_SUCCESS 등 재사용)
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_GET_SUCCESS, result);
    }

    @Operation(
            summary = "사용자 자서전 단건(질문 기준) 조회",
            description = "지정한 questionId에 대해 질문 + 사용자의 답변 1건 + 이전/다음 질문 id를 반환합니다. " +
                    "indexId로 범위를 제한할 수 있습니다."
    )
    @GetMapping("/users/{userId}/chapters/{chapterId}/questions/{questionId}")
    public ApiResponse<QuestionCurrentResponse> getUserQuestionDetail(
            @PathVariable Long userId,
            @PathVariable Long chapterId,
            @PathVariable Long questionId,
            @RequestParam(name = "indexId", required = false) Long indexId
    ) {
        var result = questionQueryService.getUserQuestionDetail(userId, chapterId, questionId, indexId);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_GET_SUCCESS, result);
    }

    @Operation(
            summary = "사용자 자서전 양페이지(연속 n개) 조회",
            description = "앵커 질문부터 연속 size개(기본 2)를 질문+답변으로 반환하고, 이전/다음 앵커 id를 함께 제공합니다."
    )
    @GetMapping("/users/{userId}/chapters/{chapterId}/pages")
    public ApiResponse<QuestionPagesResponse> getPages(
            @PathVariable Long userId,
            @PathVariable Long chapterId,
            @RequestParam(name = "indexId", required = false) Long indexId,
            @RequestParam(name = "anchorQuestionId", required = false) Long anchorQuestionId,
            @RequestParam(name = "size", required = false, defaultValue = "2") Integer size
    ) {
        var result = questionQueryService.getUserPages(userId, chapterId, indexId, anchorQuestionId, size);
        return ApiResponse.onSuccess(SuccessStatus.QUESTION_LIST_SUCCESS, result);
    }
}