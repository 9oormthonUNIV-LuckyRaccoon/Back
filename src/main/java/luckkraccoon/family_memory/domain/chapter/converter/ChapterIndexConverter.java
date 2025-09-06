package luckkraccoon.family_memory.domain.chapter.converter;

import luckkraccoon.family_memory.domain.chapter.dto.response.ChapterIndexListResponse;
import luckkraccoon.family_memory.domain.chapter.entity.ChapterIndex;

import java.util.List;

public final class ChapterIndexConverter {

    private ChapterIndexConverter() {}

    public static ChapterIndexListResponse.Item toItem(ChapterIndex idx) {
        return ChapterIndexListResponse.Item.builder()
                .id(idx.getId())
                .indexName(idx.getIndexName())
                .indexComment(idx.getIndexComment())
                .build();
    }

    public static ChapterIndexListResponse toList(Long chapterId,
                                                  String chapterName,
                                                  List<ChapterIndex> rows) {
        List<ChapterIndexListResponse.Item> items = rows.stream()
                .map(ChapterIndexConverter::toItem)
                .toList();

        return ChapterIndexListResponse.builder()
                .chapterId(chapterId)
                .chapterName(chapterName)
                .total(items.size())
                .indexes(items)
                .build();
    }
}