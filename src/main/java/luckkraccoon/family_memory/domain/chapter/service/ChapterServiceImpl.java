package luckkraccoon.family_memory.domain.chapter.service;

import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import luckkraccoon.family_memory.domain.chapter.converter.ChapterConverter;
import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterListResponse;
import luckkraccoon.family_memory.domain.chapter.entity.Chapter;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterRepository;
import luckkraccoon.family_memory.domain.chapter.handler.ChapterHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    private static final int MAX_SIZE = 6;
    private static final Set<String> ALLOWED_SORT = Set.of("createdAt", "chapterName");

    @Override
    public ChapterListResponse getChapterList(Integer sizeParam, String sortParam, String q) {
        // 1) size 검증 (기본 6, 1~6, 6 초과는 6으로 강제)
        int size = (sizeParam == null) ? MAX_SIZE : sizeParam;
        if (size < 1) throw new ChapterHandler(ErrorStatus.BAD_REQUEST); // "요청 파라미터가 유효하지 않습니다."
        if (size > MAX_SIZE) size = MAX_SIZE;

        // 2) sort 파싱/검증 (기본 createdAt,asc)
        String usedSort = (sortParam == null || sortParam.isBlank()) ? "createdAt,asc" : sortParam;
        String[] parts = usedSort.split(",", 2);
        String prop = parts[0].trim();
        String dir  = (parts.length > 1 ? parts[1].trim() : "asc").toLowerCase();

        if (!ALLOWED_SORT.contains(prop) || !(dir.equals("asc") || dir.equals("desc"))) {
            throw new ChapterHandler(ErrorStatus.BAD_REQUEST); // 허용되지 않은 정렬

        }
        Sort.Direction direction = dir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(0, size, Sort.by(direction, prop));

        // 3) 조회 (q 있으면 LIKE, 없으면 전체)
        final List<Chapter> list;
        if (q == null || q.isBlank()) {
            list = chapterRepository.findAll(pageable).getContent();
        } else {
            list = chapterRepository.findByChapterNameContainingIgnoreCase(q.trim(), pageable).getContent();
        }

        // 4) 변환 & 반환
        return ChapterConverter.toListResponse(list, prop + "," + dir);
    }
}