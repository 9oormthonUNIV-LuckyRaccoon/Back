package luckkraccoon.family_memory.domain.chapter.service;

import luckkraccoon.family_memory.domain.chapter.dto.response.UserChaptersResponse;

public interface UserChapterQueryService {
    UserChaptersResponse getUserChapters(Long userId, String stateParam);
}