package luckkraccoon.family_memory.domain.chapter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import luckkraccoon.family_memory.domain.chapter.converter.UserChapterConverter;
import luckkraccoon.family_memory.domain.chapter.dto.response.UserChaptersResponse;
import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.chapter.repository.UserChapterRepository;
import luckkraccoon.family_memory.domain.model.enums.UserChapterState;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChapterQueryServiceImpl implements UserChapterQueryService {

    private final UserRepository userRepository;
    private final UserChapterRepository userChapterRepository;

    @Override
    public UserChaptersResponse getUserChapters(Long userId, String stateParam) {
        // 1) 사용자 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND));

        // 2) state 파싱 (기본 PROGRESS / ALL 허용)
        String used = (stateParam == null || stateParam.isBlank())
                ? "PROGRESS"
                : stateParam.trim().toUpperCase();

        final List<UserChapter> rows;
        if ("ALL".equals(used)) {
            rows = userChapterRepository.findAllByUserIdWithChapter(userId);
        } else if ("PROGRESS".equals(used) || "SUCCESS".equals(used)) {
            UserChapterState state = UserChapterState.valueOf(used);
            rows = userChapterRepository.findAllByUserIdAndStateWithChapter(userId, state);
        } else {
            // 잘못된 state 값
            throw new UserHandler(ErrorStatus.BAD_REQUEST);
        }

        return UserChapterConverter.toList(userId, used, rows);
    }
}