package luckkraccoon.family_memory.domain.chapter.converter;

import luckkraccoon.family_memory.domain.chapter.dto.response.UserChaptersResponse;
import luckkraccoon.family_memory.domain.chapter.entity.Chapter;
import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;

import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static luckkraccoon.family_memory.domain.chapter.dto.response.UserChaptersResponse.*;

public class UserChapterConverter {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static UserChaptersResponse toList(Long userId, String state, List<UserChapter> rows) {
        return UserChaptersResponse.builder()
                .userId(userId)
                .state(state)
                .items(rows.stream().map(UserChapterConverter::toItem).toList())
                .build();
    }

    private static UserChaptersResponse.UserChapterItem toItem(UserChapter uc) {
        Chapter ch = uc.getChapter();

        Integer progress = tryGetProgressPercent(uc);
        Long lastQid     = tryGetLastQuestionId(uc);
        String finished  = tryGetFinishedAtIso(uc);
        String updated   = uc.getUpdatedAt() == null ? null : uc.getUpdatedAt().atOffset(java.time.ZoneOffset.UTC).format(ISO);

        return UserChaptersResponse.UserChapterItem.builder()
                .userChapterId(uc.getId())
                .state(uc.getState() == null ? "NONE" : uc.getState().name())
                .progressPercent(progress == null ? 0 : progress)
                .lastQuestionId(lastQid)
                .finishedAt(finished)
                .updatedAt(updated)
                .chapter(ChapterSummary.builder()
                        .id(ch.getId())
                        .chapterName(ch.getChapterName())
                        .chapterComment(ch.getChapterComment())
                        .build())
                .build();
    }

    /** 엔티티에 필드명이 다를 수 있어 리플렉션으로 안전 접근(없으면 0 처리) */
    private static Integer tryGetProgressPercent(UserChapter uc) {
        Integer v = (Integer) tryCall(uc, "getProgressPercent");
        if (v != null) return v;
        Number n = (Number) tryCall(uc, "getProgress"); // 다른 프로젝트명 호환
        return n == null ? null : n.intValue();
    }

    private static Long tryGetLastQuestionId(UserChapter uc) {
        Object q = tryCall(uc, "getLastQuestion");
        if (q != null) {
            Object id = tryCall(q, "getId");
            return id instanceof Number ? ((Number) id).longValue() : null;
        }
        Number n = (Number) tryCall(uc, "getLastQuestionId");
        return n == null ? null : n.longValue();
    }

    private static String tryGetFinishedAtIso(UserChapter uc) {
        Object t = tryCall(uc, "getFinishedAt");
        if (t == null) return null;
        try {
            var temporal = (java.time.LocalDateTime) t;
            return temporal.atOffset(java.time.ZoneOffset.UTC).format(ISO);
        } catch (ClassCastException ignore) { return null; }
    }

    private static Object tryCall(Object target, String method) {
        try {
            Method m = target.getClass().getMethod(method);
            return m.invoke(target);
        } catch (Exception ignore) {
            return null;
        }
    }
}