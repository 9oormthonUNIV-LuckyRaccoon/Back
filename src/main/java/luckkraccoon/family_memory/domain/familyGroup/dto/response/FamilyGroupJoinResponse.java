package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyGroupJoinResponse {
    private Long userId;
    private Long groupId;
    private String groupJoinId;
    private String groupName;
    private Integer currentCount;
    private Integer groupMaxCount; // 엔티티가 groupCount 라면 여기엔 그 값을 넣음
    private String joinedAt;       // ISO-8601
}