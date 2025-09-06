package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyGroupGetResponse {
    private Long id;
    private String groupJoinId;
    private String groupName;
    private String groupComment;
    private String groupImage;
    private Integer groupMaxCount; // 엔티티의 groupCount를 여기에 매핑
    private Integer currentCount;
    private String createdAt;      // ISO-8601(LocalDateTime.toString)
    private String updatedAt;
}