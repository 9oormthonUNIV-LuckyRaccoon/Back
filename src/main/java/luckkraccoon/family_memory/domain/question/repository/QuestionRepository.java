package luckkraccoon.family_memory.domain.question.repository;

import luckkraccoon.family_memory.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByIndexId(Long indexId);
}
