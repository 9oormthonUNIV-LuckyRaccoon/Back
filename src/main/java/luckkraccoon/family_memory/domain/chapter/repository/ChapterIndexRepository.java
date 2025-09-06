package luckkraccoon.family_memory.domain.chapter.repository;

import luckkraccoon.family_memory.domain.chapter.entity.ChapterIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterIndexRepository extends JpaRepository<ChapterIndex, Long> {
    List<ChapterIndex> findByChapterId(Long chapterId);
}
