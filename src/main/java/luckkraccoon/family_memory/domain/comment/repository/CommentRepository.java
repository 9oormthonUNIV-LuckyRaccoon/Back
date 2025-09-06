package luckkraccoon.family_memory.domain.comment.repository;

import luckkraccoon.family_memory.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserChapterId(Long userChapterId);
}
