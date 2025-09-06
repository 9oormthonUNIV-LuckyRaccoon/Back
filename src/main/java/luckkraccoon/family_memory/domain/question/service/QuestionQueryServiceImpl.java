package luckkraccoon.family_memory.domain.question.service;

import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.chapter.entity.Chapter;
import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterRepository;
import luckkraccoon.family_memory.domain.chapter.repository.ChapterIndexRepository;
import luckkraccoon.family_memory.domain.chapter.repository.UserChapterRepository;
import luckkraccoon.family_memory.domain.model.enums.UserChapterState;
import luckkraccoon.family_memory.domain.question.converter.QuestionConverter;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionCurrentResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionDetailResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionListResponse;
import luckkraccoon.family_memory.domain.question.dto.response.QuestionPagesResponse;
import luckkraccoon.family_memory.domain.question.entity.Question;
import luckkraccoon.family_memory.domain.question.entity.UserQuestion;
import luckkraccoon.family_memory.domain.question.handler.QuestionHandler;
import luckkraccoon.family_memory.domain.question.repository.QuestionRepository;
import luckkraccoon.family_memory.domain.chapter.handler.ChapterHandler;
import luckkraccoon.family_memory.domain.question.repository.UserQuestionRepository;
import luckkraccoon.family_memory.domain.user.entity.User;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterIndexRepository chapterIndexRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final UserChapterRepository userChapterRepository;
    private final UserRepository userRepository;

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

    @Override
    @Transactional(readOnly = true)
    public QuestionCurrentResponse getCurrentPosition(Long userId, Long chapterId, Long indexId) {
        // 1) 유저/챕터 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));

        // 2) UserChapter 로드(없으면 생성: NONE, 0%, lastQuestion=null)
        UserChapter uc = userChapterRepository.findByUser_IdAndChapter_Id(user.getId(), chapter.getId())
                .orElseGet(() -> userChapterRepository.save(
                        UserChapter.builder()
                                .user(user)
                                .chapter(chapter)
                                .state(UserChapterState.NONE)
                                .progressPercent(0)
                                .lastQuestion(null)
                                .finishedAt(null)
                                .build()
                ));

        // 3) 현재 질문 결정
        Question current = resolveCurrentQuestion(uc, chapterId, indexId);

        if (current == null) {
            // 범위 내 질문 자체가 없는 경우
            throw new QuestionHandler(ErrorStatus.NOT_FOUND);
        }

        // 4) 사용자의 해당 질문 답변(있으면)
        UserQuestion myAnswer = userQuestionRepository
                .findByUser_IdAndQuestion_Id(user.getId(), current.getId())
                .orElse(null);

        // 5) nav 계산 (prev / next)
        Long prevId = findPrevId(current.getId(), chapterId, indexId);
        Long nextId = findNextId(current.getId(), chapterId, indexId);

        // 6) 응답 변환
        return QuestionConverter.toCurrentResponse(
                user.getId(), chapter.getId(), indexId,
                current, myAnswer, prevId, nextId
        );
    }

    private Question resolveCurrentQuestion(UserChapter uc, Long chapterId, Long indexId) {
        // 범위: indexId가 있으면 목차 범위, 없으면 챕터 범위
        Long lastId = (uc.getLastQuestion() == null) ? null : uc.getLastQuestion().getId();

        if (lastId == null) {
            // 첫 진입 → 해당 범위의 "첫 질문"
            return (indexId != null)
                    ? questionRepository.findFirstByIndex_IdOrderByIdAsc(indexId).orElse(null)
                    : questionRepository.findFirstByIndex_Chapter_IdOrderByIdAsc(chapterId).orElse(null);
        }

        // lastId가 범위 내인지 검증
        Optional<Question> last = questionRepository.findById(lastId);
        if (last.isPresent()) {
            Question q = last.get();
            boolean inScope = (indexId != null)
                    ? (q.getIndex() != null
                    && q.getIndex().getId().equals(indexId)
                    && q.getIndex().getChapter().getId().equals(chapterId))
                    : (q.getIndex() != null
                    && q.getIndex().getChapter().getId().equals(chapterId));
            if (inScope) return q;
        }

        // 범위 밖이면 "lastId보다 큰 다음 질문" → 없으면 "첫 질문" 폴백
        return (indexId != null)
                ? questionRepository.findFirstByIndex_IdAndIdGreaterThanOrderByIdAsc(indexId, lastId)
                .orElseGet(() -> questionRepository.findFirstByIndex_IdOrderByIdAsc(indexId).orElse(null))
                : questionRepository.findFirstByIndex_Chapter_IdAndIdGreaterThanOrderByIdAsc(chapterId, lastId)
                .orElseGet(() -> questionRepository.findFirstByIndex_Chapter_IdOrderByIdAsc(chapterId).orElse(null));
    }

    private Long findPrevId(Long currentId, Long chapterId, Long indexId) {
        return (indexId != null)
                ? questionRepository.findFirstByIndex_IdAndIdLessThanOrderByIdDesc(indexId, currentId)
                .map(Question::getId).orElse(null)
                : questionRepository.findFirstByIndex_Chapter_IdAndIdLessThanOrderByIdDesc(chapterId, currentId)
                .map(Question::getId).orElse(null);
    }

    private Long findNextId(Long currentId, Long chapterId, Long indexId) {
        return (indexId != null)
                ? questionRepository.findFirstByIndex_IdAndIdGreaterThanOrderByIdAsc(indexId, currentId)
                .map(Question::getId).orElse(null)
                : questionRepository.findFirstByIndex_Chapter_IdAndIdGreaterThanOrderByIdAsc(chapterId, currentId)
                .map(Question::getId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionCurrentResponse getUserQuestionDetail(Long userId, Long chapterId, Long questionId, Long indexId) {
        // 1) 유저/챕터 존재 확인
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
        var chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));

        // 2) 질문 로드 + 범위 일치 검증 (chapterId & optional indexId)
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));

        // 질문은 항상 어떤 index(=chapterIndex)에 속한다.
        var qIndex = question.getIndex();
        var qChapterId = qIndex.getChapter().getId();

        boolean inScope = qChapterId.equals(chapter.getId())
                && (indexId == null || qIndex.getId().equals(indexId));

        if (!inScope) {
            // “요청한 질문을 찾을 수 없거나 챕터/목차와 일치하지 않습니다.”
            throw new QuestionHandler(ErrorStatus.NOT_FOUND);
        }

        // 3) 사용자의 해당 질문 답변 (없으면 null 허용)
        var userAnswer = userQuestionRepository
                .findByUser_IdAndQuestion_Id(user.getId(), question.getId())
                .orElse(null);

        // 4) prev / next (동일 범위)
        Long prevId;
        Long nextId;
        if (indexId != null) {
            prevId = questionRepository
                    .findFirstByIndex_IdAndIdLessThanOrderByIdDesc(indexId, question.getId())
                    .map(l -> l.getId()).orElse(null);
            nextId = questionRepository
                    .findFirstByIndex_IdAndIdGreaterThanOrderByIdAsc(indexId, question.getId())
                    .map(l -> l.getId()).orElse(null);
        } else {
            prevId = questionRepository
                    .findFirstByIndex_Chapter_IdAndIdLessThanOrderByIdDesc(chapterId, question.getId())
                    .map(l -> l.getId()).orElse(null);
            nextId = questionRepository
                    .findFirstByIndex_Chapter_IdAndIdGreaterThanOrderByIdAsc(chapterId, question.getId())
                    .map(l -> l.getId()).orElse(null);
        }

        // 5) DTO 변환
        return QuestionConverter.toCurrentResponse(
                user.getId(), chapter.getId(), indexId,
                question, userAnswer, prevId, nextId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionPagesResponse getUserPages(Long userId, Long chapterId, Long indexId,
                                              Long anchorQuestionId, Integer sizeParam) {

        // 0) 파라미터 검증
        int size = (sizeParam == null) ? 2 : sizeParam;
        if (size < 1 || size > 10) throw new QuestionHandler(ErrorStatus.BAD_REQUEST);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
        var chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));

        // 1) 앵커 결정
        Long anchorId = null;

        // 요청 앵커가 우선
        if (anchorQuestionId != null) {
            var anchorQ = questionRepository.findById(anchorQuestionId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
            boolean inScope = anchorQ.getIndex().getChapter().getId().equals(chapterId)
                    && (indexId == null || anchorQ.getIndex().getId().equals(indexId));
            if (inScope) anchorId = anchorQ.getId();
        }

        // 요청 앵커가 없거나 범위 밖이면, 사용자 현재 위치
        if (anchorId == null) {
            var uc = userChapterRepository.findByUser_IdAndChapter_Id(userId, chapterId).orElse(null);
            if (uc != null && uc.getLastQuestion() != null) {
                var last = uc.getLastQuestion();
                boolean inScope = last.getIndex().getChapter().getId().equals(chapterId)
                        && (indexId == null || last.getIndex().getId().equals(indexId));
                if (inScope) anchorId = last.getId();
            }
        }

        // 그래도 없으면 범위의 첫 질문
        if (anchorId == null) {
            anchorId = (indexId == null)
                    ? questionRepository.findFirstByIndex_Chapter_IdOrderByIdAsc(chapterId)
                    .map(Question::getId).orElse(null)
                    : questionRepository.findFirstByIndex_IdOrderByIdAsc(indexId)
                    .map(Question::getId).orElse(null);
        }

        if (anchorId == null) {
            // 범위 내 질문 자체가 없음
            throw new QuestionHandler(ErrorStatus.NOT_FOUND);
        }

        // 2) 앵커부터 연속 size개 로딩
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Question> questions = (indexId == null)
                ? questionRepository.findByIndex_Chapter_IdAndIdGreaterThanEqual(chapterId, anchorId, pageable).getContent()
                : questionRepository.findByIndex_IdAndIdGreaterThanEqual(indexId, anchorId, pageable).getContent();

        if (questions.isEmpty()) throw new QuestionHandler(ErrorStatus.NOT_FOUND);

        // 3) 사용자 답변 맵
        List<Long> qIds = questions.stream().map(Question::getId).toList();
        Map<Long, UserQuestion> answerMap = userQuestionRepository
                .findByUser_IdAndQuestion_IdIn(user.getId(), qIds)
                .stream()
                .collect(Collectors.toMap(uq -> uq.getQuestion().getId(), Function.identity()));

        // 4) pageNav 계산
        Long firstId = questions.get(0).getId();
        Long lastId  = questions.get(questions.size() - 1).getId();

        Long prevAnchor = (indexId == null)
                ? questionRepository.findFirstByIndex_Chapter_IdAndIdLessThanOrderByIdDesc(chapterId, firstId)
                .map(Question::getId).orElse(null)
                : questionRepository.findFirstByIndex_IdAndIdLessThanOrderByIdDesc(indexId, firstId)
                .map(Question::getId).orElse(null);

        Long nextAnchor = (indexId == null)
                ? questionRepository.findFirstByIndex_Chapter_IdAndIdGreaterThanOrderByIdAsc(chapterId, lastId)
                .map(Question::getId).orElse(null)
                : questionRepository.findFirstByIndex_IdAndIdGreaterThanOrderByIdAsc(indexId, lastId)
                .map(Question::getId).orElse(null);

        // 5) DTO 변환
        return QuestionConverter.toPagesResponse(
                userId, chapterId, indexId, size,
                questions, answerMap,
                prevAnchor, nextAnchor
        );
    }
}