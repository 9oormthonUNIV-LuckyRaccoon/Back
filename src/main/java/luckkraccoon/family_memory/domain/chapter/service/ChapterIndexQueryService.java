package luckkraccoon.family_memory.domain.chapter.service;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterIndexListResponse;

public interface ChapterIndexQueryService {
    ChapterIndexListResponse getChapterIndexes(Long chapterId);
}