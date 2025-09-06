package luckkraccoon.family_memory.domain.chapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterListResponse;
import luckkraccoon.family_memory.domain.chapter.service.ChapterService;
import luckkraccoon.family_memory.global.common.response.ApiResponse;
import luckkraccoon.family_memory.global.error.code.status.SuccessStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Chapter", description = "챕터 API")
public class ChapterController {

    private final ChapterService chapterService;

    @Operation(summary = "챕터 목록 조회", description = "최대 6개까지 조회. size(1~6), sort(createdAt,asc|chapterName,asc), q 검색 지원.")
    @GetMapping(value = "/chapter", produces = "application/json")
    public ResponseEntity<ApiResponse<ChapterListResponse>> getChapters(
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "q",    required = false) String q
    ) {
        ChapterListResponse result = chapterService.getChapterList(size, sort, q);

        // 비어 있으면 204 메시지로 응답(HTTP 200 + code "204" 패턴 유지)
        if (result.getSize() == 0) {
            return ResponseEntity.ok(
                    ApiResponse.onSuccess(SuccessStatus.CHAPTER_LIST_EMPTY, result)
            );
        }
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.CHAPTER_LIST_SUCCESS, result)
        );
    }
}