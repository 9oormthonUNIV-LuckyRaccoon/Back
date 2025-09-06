package luckkraccoon.family_memory.domain.chapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterIndexListResponse;
import luckkraccoon.family_memory.domain.chapter.service.ChapterIndexQueryService;
import luckkraccoon.family_memory.global.common.response.ApiResponse;
import luckkraccoon.family_memory.global.error.code.status.SuccessStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ChapterIndex", description = "챕터 목차 API")
public class ChapterIndexController {

    private final ChapterIndexQueryService chapterIndexQueryService;

    @Operation(summary = "챕터 목차 조회", description = "지정한 챕터의 목차(ChapterIndex) 목록을 id ASC로 반환합니다.")
    @GetMapping("/chapter/{chapterId}/indexes")
    public ApiResponse<ChapterIndexListResponse> getChapterIndexes(@PathVariable Long chapterId) {
        ChapterIndexListResponse result = chapterIndexQueryService.getChapterIndexes(chapterId);
        return ApiResponse.onSuccess(SuccessStatus.CHAPTER_INDEX_LIST_SUCCESS, result);
    }

    // 보조 엔드포인트 원하면 활성화
    // @GetMapping("/chapter-index")
    // public ApiResponse<ChapterIndexListResponse> getChapterIndexesByQuery(@RequestParam Long chapterId) {
    //     var result = chapterIndexQueryService.getChapterIndexes(chapterId);
    //     return ApiResponse.onSuccess(SuccessStatus.CHAPTER_INDEX_LIST_SUCCESS, result);
    // }
}