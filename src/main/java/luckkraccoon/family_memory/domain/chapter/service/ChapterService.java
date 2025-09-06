package luckkraccoon.family_memory.domain.chapter.service;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterListResponse;

public interface ChapterService {
    ChapterListResponse getChapterList(Integer size, String sort, String q);

}
