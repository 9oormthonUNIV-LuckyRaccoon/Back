package luckkraccoon.family_memory.domain.user.repository;

import luckkraccoon.family_memory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    // ✅ 그룹의 모든 사용자 조회 (페이지네이션 없음)
    //   - 두 이름 중 하나만 사용해도 되지만, 혼동 방지를 위해 둘 중 하나로 통일하세요.
    //   - 서비스에서 'findByFamilyGroup_Id'를 쓴다면 이 메서드가 꼭 있어야 합니다.
    List<User> findByFamilyGroup_Id(Long groupId);
    // 또는 이 형태를 쓰려면 서비스에서도 'findByFamilyGroupId'로 호출해야 합니다.
    // List<User> findByFamilyGroupId(Long groupId);

    // ✅ 검색(q) 지원: 닉네임/이름/로그인아이디 LIKE
    @Query("""
        select u from User u
        where u.familyGroup.id = :groupId
          and (
            lower(u.nickName) like lower(concat('%', :kw, '%'))
            or lower(u.userName) like lower(concat('%', :kw, '%'))
            or lower(u.userId)   like lower(concat('%', :kw, '%'))
          )
        """)
    List<User> searchMembersList(@Param("groupId") Long groupId,
                                 @Param("kw") String keyword);

    // ✅ 현재 인원 수 (서버 기준)
    long countByFamilyGroup_Id(Long groupId);
    // 또는 countByFamilyGroupId(Long groupId);

}
