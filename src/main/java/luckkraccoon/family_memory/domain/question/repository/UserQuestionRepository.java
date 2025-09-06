package luckkraccoon.family_memory.domain.question.repository;

import luckkraccoon.family_memory.domain.question.entity.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    List<UserQuestion> findByUserId(Long userId);
    List<UserQuestion> findByQuestionId(Long questionId);
}
