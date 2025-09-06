package luckkraccoon.family_memory.domain.familyGroup.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyGroupCreateResponse {
    private Long id;
    private String groupJoinId;
    private String groupName;
    private String groupComment;
    private String groupImage;
    private Integer groupMaxCount;
    private Integer currentCount;
    private Long ownerUserId;
    private String createdAt; // ISO-8601
}