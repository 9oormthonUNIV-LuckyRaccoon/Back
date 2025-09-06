package luckkraccoon.family_memory.domain.familyGroup.repository;

import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {
    Optional<FamilyGroup> findByGroupJoinId(String groupJoinId);
    boolean existsByGroupJoinId(String groupJoinId);
}
