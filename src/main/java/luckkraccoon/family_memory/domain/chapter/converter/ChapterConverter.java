package luckkraccoon.family_memory.domain.chapter.converter;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterListResponse;
import luckkraccoon.family_memory.domain.chapter.entity.Chapter;

import java.util.List;

public final class ChapterConverter {

    private ChapterConverter() {}

    public static ChapterListResponse.Item toItem(Chapter c) {
        return ChapterListResponse.Item.builder()
                .id(c.getId())
                .chapterName(c.getChapterName())
                .chapterComment(c.getChapterComment())
                .createdAt(c.getCreatedAt() == null ? null : c.getCreatedAt().toString())
                .updatedAt(c.getUpdatedAt() == null ? null : c.getUpdatedAt().toString())
                .build();
    }

    public static ChapterListResponse toListResponse(List<Chapter> chapters, String sortUsed) {
        List<ChapterListResponse.Item> items = chapters.stream()
                .map(ChapterConverter::toItem)
                .toList();

        return ChapterListResponse.builder()
                .size(items.size())
                .sort(sortUsed)
                .chapters(items)
                .build();
    }
}