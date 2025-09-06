package luckkraccoon.family_memory.domain.question.repository;

import luckkraccoon.family_memory.domain.question.entity.Question;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends Repository<Question, Long> {
    List<Question> findByIndexId(Long indexId);
    /** 챕터 전체(정렬 지원) */
    List<Question> findByIndex_Chapter_Id(Long chapterId, Sort sort);

    /** 검색/필터 통합 쿼리 (정렬 지원) */
    @Query("""
        select q
          from Question q
         where q.index.chapter.id = :chapterId
           and (:indexId is null or q.index.id = :indexId)
           and (
                :kw is null
             or lower(q.questionName)    like lower(concat('%', :kw, '%'))
             or lower(q.questionComment) like lower(concat('%', :kw, '%'))
           )
        """)
    List<Question> searchForList(@Param("chapterId") Long chapterId,
                                 @Param("indexId")   Long indexId,
                                 @Param("kw")        String kw,
                                 Sort sort);
}