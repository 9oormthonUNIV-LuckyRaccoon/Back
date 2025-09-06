package luckkraccoon.family_memory.domain.chapter.repository;

import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChapterRepository extends JpaRepository<UserChapter, Long> {
    List<UserChapter> findByUserId(Long userId);
    Optional<UserChapter> findByUserIdAndChapterId(Long userId, Long chapterId);
}
