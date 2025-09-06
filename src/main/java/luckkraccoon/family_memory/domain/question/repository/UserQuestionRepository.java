package luckkraccoon.family_memory.domain.question.repository;

import luckkraccoon.family_memory.domain.question.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    List<UserQuestion> findByUserId(Long userId);
    List<UserQuestion> findByQuestionId(Long questionId);
    Optional<UserQuestion> findByUser_IdAndQuestion_Id(Long userId, Long questionId);

    long countDistinctByUser_IdAndQuestion_Index_Chapter_Id(Long userId, Long chapterId);
}
