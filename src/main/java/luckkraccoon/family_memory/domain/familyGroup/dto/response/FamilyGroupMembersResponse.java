package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FamilyGroupMembersResponse {
    private Long groupId;
    private String groupName;
    private Integer currentCount;            // 서버 기준(실시간 users count)
    private List<Member> members;

    @Getter
    @Builder
    public static class Member {
        private Long id;                     // users.id
        private String userId;               // 로그인 아이디
        private String nickName;
        private String userName;
        private String userImage;
        private String gender;               // "MALE"/"FEMALE"/null
        private String birth;                // yyyy-MM-dd / null
        private String createdAt;            // ISO-8601 / null
    }
}