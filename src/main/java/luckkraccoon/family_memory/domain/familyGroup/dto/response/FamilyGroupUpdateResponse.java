package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyGroupUpdateResponse {
    private Long id;
    private String groupJoinId;
    private String groupName;
    private String groupComment;
    private String groupImage;
    private Integer groupMaxCount;   // = entity.groupCount
    private Integer currentCount;
    private String updatedAt;        // ISO-8601
}