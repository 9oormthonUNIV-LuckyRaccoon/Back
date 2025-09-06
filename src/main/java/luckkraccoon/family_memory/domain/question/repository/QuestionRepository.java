package luckkraccoon.family_memory.domain.question.repository;

import luckkraccoon.family_memory.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends Repository<Question, Long> {
    Optional<Question> findById(Long id);
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

    /** 단건 상세 조회: index 및 chapter까지 한 번에 페치 */
    @Query("""
           select q
             from Question q
             join fetch q.index i
             join fetch i.chapter c
            where q.id = :id
           """)
    Optional<Question> findDetailById(@Param("id") Long id);

    long countByIndex_Chapter_Id(Long chapterId);

    // ====== 추가: 첫/이전/다음 (chapter 범위) ======
    Optional<Question> findFirstByIndex_Chapter_IdOrderByIdAsc(Long chapterId);
    Optional<Question> findFirstByIndex_Chapter_IdAndIdGreaterThanOrderByIdAsc(Long chapterId, Long id);
    Optional<Question> findFirstByIndex_Chapter_IdAndIdLessThanOrderByIdDesc(Long chapterId, Long id);

    // ====== 추가: 첫/이전/다음 (index 범위) ======
    Optional<Question> findFirstByIndex_IdOrderByIdAsc(Long indexId);
    Optional<Question> findFirstByIndex_IdAndIdGreaterThanOrderByIdAsc(Long indexId, Long id);
    Optional<Question> findFirstByIndex_IdAndIdLessThanOrderByIdDesc(Long indexId, Long id);


    Page<Question> findByIndex_Chapter_IdAndIdGreaterThanEqual(Long chapterId, Long id, Pageable pageable);
    Page<Question> findByIndex_IdAndIdGreaterThanEqual(Long indexId, Long id, Pageable pageable);

}