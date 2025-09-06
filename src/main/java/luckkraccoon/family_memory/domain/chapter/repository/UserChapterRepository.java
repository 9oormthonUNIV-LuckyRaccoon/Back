package luckkraccoon.family_memory.domain.chapter.repository;

import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.model.enums.UserChapterState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChapterRepository extends JpaRepository<UserChapter, Long> {
    List<UserChapter> findByUserId(Long userId);
    Optional<UserChapter> findByUserIdAndChapterId(Long userId, Long chapterId);

    @Query("""
           select uc
             from UserChapter uc
             join fetch uc.chapter c
            where uc.user.id = :userId
            order by uc.updatedAt desc
           """)
    List<UserChapter> findAllByUserIdWithChapter(@Param("userId") Long userId);

    @Query("""
           select uc
             from UserChapter uc
             join fetch uc.chapter c
            where uc.user.id = :userId
              and uc.state = :state
            order by uc.updatedAt desc
           """)
    List<UserChapter> findAllByUserIdAndStateWithChapter(@Param("userId") Long userId,
                                                         @Param("state") UserChapterState state);

    // 존재 확인용
    boolean existsById(Long id); // (선택)
    Optional<UserChapter> findByUser_IdAndChapter_Id(Long userId, Long chapterId);

    UserChapter save(UserChapter entity);

}
