package luckkraccoon.family_memory.domain.question.service;

import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterRepository;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterIndexRepository;
import luckkraccoon.family_memory.domain.question.converter.QuestionConverter;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionDetailResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;
import luckkraccoon.family_memory.domain.question.entity.Question;
import luckkraccoon.family_memory.domain.question.handler.QuestionHandler;
import luckkraccoon.family_memory.domain.question.repository.QuestionRepository;
import luckkraccoon.family_memory.domain.chapter.handler.ChapterHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterIndexRepository chapterIndexRepository;

    private static final Set<String> ALLOWED_SORT = Set.of("id", "questionName");

    @Override
    public QuestionListResponse getQuestions(Long chapterId, Long indexId, String q, String sortParam) {

        // 1) 챕터 존재 검증
        if (!chapterRepository.existsById(chapterId)) {
            throw new ChapterHandler(ErrorStatus.NOT_FOUND); // "챕터를 찾을 수 없습니다."
        }

        // 2) indexId가 있으면 챕터와의 일치 검증
        if (indexId != null && !chapterIndexRepository.existsByIdAndChapter_Id(indexId, chapterId)) {
            throw new ChapterHandler(ErrorStatus.NOT_FOUND); // "해당 목차를 찾을 수 없거나 챕터와 일치하지 않습니다."
        }

        // 3) sort 파싱/검증 (기본 id,asc)
        String usedSort = (sortParam == null || sortParam.isBlank()) ? "id,asc" : sortParam;
        String[] parts  = usedSort.split(",", 2);
        String prop     = parts[0].trim();
        String dir      = (parts.length > 1 ? parts[1].trim() : "asc").toLowerCase();

        if (!ALLOWED_SORT.contains(prop) || !(dir.equals("asc") || dir.equals("desc"))) {
            throw new ChapterHandler(ErrorStatus.BAD_REQUEST); // "요청 파라미터가 유효하지 않습니다."
        }
        Sort.Direction direction = dir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, prop);

        // 4) 조회
        final List<Question> list;
        String kw = (q == null || q.isBlank()) ? null : q.trim();

        if (kw != null || indexId != null) {
            list = questionRepository.searchForList(chapterId, indexId, kw, sort);
        } else {
            list = questionRepository.findByIndex_Chapter_Id(chapterId, sort);
        }

        // 5) 변환 & 반환
        return QuestionConverter.toListResponse(chapterId, indexId, list);
    }


    @Override
    public QuestionDetailResponse getQuestion(Long id) {
        Question q = questionRepository.findDetailById(id)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
        return QuestionConverter.toDetail(q);
    }
}