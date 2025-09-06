package luckkraccoon.family_memory.domain.chapter.repository;

import luckkraccoon.family_memory.domain.chapter.entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Page<Chapter> findByChapterNameContainingIgnoreCase(String keyword, Pageable pageable);

}
