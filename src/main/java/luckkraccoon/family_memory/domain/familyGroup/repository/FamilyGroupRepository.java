package luckkraccoon.family_memory.domain.familyGroup.repository;

import jakarta.persistence.LockModeType;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
    Optional<FamilyGroup> findByGroupJoinId(String groupJoinId);
    boolean existsByGroupJoinId(String groupJoinId);

    // 동시 참여 요청을 안전하게 처리하기 위한 행 잠금
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from FamilyGroup g where g.groupJoinId = :groupJoinId")
    Optional<FamilyGroup> findForUpdateByGroupJoinId(@Param("groupJoinId") String groupJoinId);

}
