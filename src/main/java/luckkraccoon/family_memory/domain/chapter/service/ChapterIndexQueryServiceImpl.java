package luckkraccoon.family_memory.domain.chapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import luckkraccoon.family_memory.domain.chapter.converter.ChapterIndexConverter;
import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterIndexListResponse;
import luckkraccoon.family_memory.domain.chapter.entity.Chapter;
import luckkraccoon.family_memory.domain.chapter.entity.ChapterIndex;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterIndexRepository;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterRepository;
import luckkraccoon.family_memory.domain.chapter.handler.ChapterHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterIndexQueryServiceImpl implements ChapterIndexQueryService {

    private final ChapterRepository chapterRepository;
    private final ChapterIndexRepository chapterIndexRepository;

    @Override
    public ChapterIndexListResponse getChapterIndexes(Long chapterId) {
        if (chapterId == null || chapterId <= 0) {
            throw new ChapterHandler(ErrorStatus.BAD_REQUEST); // "요청 값이 유효하지 않습니다."
        }

        // 챕터 존재 & 이름 확보
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ChapterHandler(ErrorStatus.NOT_FOUND)); // "챕터를 찾을 수 없습니다."

        // 목차 목록 조회 (id ASC)
        List<ChapterIndex> rows = chapterIndexRepository.findByChapter_IdOrderByIdAsc(chapterId);

        return ChapterIndexConverter.toList(chapterId, chapter.getChapterName(), rows);
    }
}