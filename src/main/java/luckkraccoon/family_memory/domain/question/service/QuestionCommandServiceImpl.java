package luckkraccoon.family_memory.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.chapter.repository.UserChapterRepository;
import luckkraccoon.family_memory.domain.model.enums.UserChapterState;
import luckkraccoon.family_memory.domain.question.converter.QuestionConverter;
import luckkraccoon.family_memory.domain.question.dto.request.AnswerCreateRequest;
import luckkraccoon.family_memory.domain.question.dto.response.AnswerCreateResponse;
import luckkraccoon.family_memory.domain.question.entity.Question;
import luckkraccoon.family_memory.domain.question.entity.UserQuestion;
import luckkraccoon.family_memory.domain.question.handler.QuestionHandler;
import luckkraccoon.family_memory.domain.question.repository.QuestionRepository;
import luckkraccoon.family_memory.domain.question.repository.UserQuestionRepository;
import luckkraccoon.family_memory.domain.user.entity.User;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final UserChapterRepository userChapterRepository;

    @Override
    public AnswerCreateResponse createOrUpdateAnswer(Long questionId, AnswerCreateRequest request) {
        // 1) 로드 & 검증
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.NOT_FOUND));

        String content = request.getContent() == null ? "" : request.getContent().trim();
        if (content.isEmpty()) throw new QuestionHandler(ErrorStatus.BAD_REQUEST);

        boolean overwrite = Boolean.TRUE.equals(request.getOverwrite());

        // 2) UserQuestion upsert (user-question 1개 정책)
        UserQuestion saved;
        UserQuestion existing = userQuestionRepository
                .findByUser_IdAndQuestion_Id(user.getId(), question.getId())
                .orElse(null);

        if (existing == null) {
            saved = userQuestionRepository.save(
                    UserQuestion.builder()
                            .user(user)
                            .question(question)
                            .answer(content)      // ← 엔티티 필드명은 answer
                            .answerVoice(null)
                            .build()
            );
        } else {
            if (!overwrite) throw new QuestionHandler(ErrorStatus.CONFLICT);
            existing.setAnswer(content);
            saved = existing; // dirty checking
        }

        // 3) UserChapter upsert/갱신
        Long chapterId = question.getIndex().getChapter().getId();

        UserChapter uc = userChapterRepository.findByUser_IdAndChapter_Id(user.getId(), chapterId)
                .orElseGet(() -> userChapterRepository.save(
                        UserChapter.builder()
                                .user(user)
                                .chapter(question.getIndex().getChapter())
                                .state(UserChapterState.PROGRESS)
                                .progressPercent(0)
                                .lastQuestion(question)   // 연관 그대로 사용
                                .finishedAt(null)
                                .build()
                ));

        uc.setLastQuestion(question);

        long total = Math.max(1, questionRepository.countByIndex_Chapter_Id(chapterId));
        long solved = userQuestionRepository.countDistinctByUser_IdAndQuestion_Index_Chapter_Id(user.getId(), chapterId);
        int progress = (int) Math.round((solved * 100.0) / total);
        uc.setProgressPercent(progress);

        if (progress >= 100) {
            uc.setState(UserChapterState.SUCCESS);
            if (uc.getFinishedAt() == null) uc.setFinishedAt(LocalDateTime.now());
        } else {
            // NONE → PROGRESS 승격
            if (uc.getState() == null || uc.getState() == UserChapterState.NONE) {
                uc.setState(UserChapterState.PROGRESS);
            }
            uc.setFinishedAt(null);
        }

        // 4) 응답
        return QuestionConverter.toAnswerCreateResponse(saved, uc);
    }
}