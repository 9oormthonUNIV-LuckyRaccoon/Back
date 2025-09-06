package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyGroupLeaveResponse {
    private Long userId;
    private Long groupId;
    private Integer currentCount; // 탈퇴 반영 후
    private String leftAt;        // ISO-8601
}