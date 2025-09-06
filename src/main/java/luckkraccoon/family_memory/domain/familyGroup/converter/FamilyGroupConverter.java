package luckkraccoon.family_memory.domain.familyGroup.converter;

import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupCreateResponse;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupGetResponse;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;

public class FamilyGroupConverter {

    public static FamilyGroupCreateResponse toCreateResponse(FamilyGroup g, Long ownerUserId) {
        return FamilyGroupCreateResponse.builder()
                .id(g.getId())
                .groupJoinId(g.getGroupJoinId())
                .groupName(g.getGroupName())
                .groupComment(g.getGroupComment())
                .groupImage(g.getGroupImage())
                .groupMaxCount(g.getGroupCount() != null ? g.getGroupCount() : null)
                .currentCount(g.getCurrentCount())
                .ownerUserId(ownerUserId)
                .createdAt(g.getCreatedAt() == null ? null : g.getCreatedAt().toString())
                .build();
    }


    public static FamilyGroupGetResponse toGetResponse(FamilyGroup g) {
        return FamilyGroupGetResponse.builder()
                .id(g.getId())
                .groupJoinId(g.getGroupJoinId())
                .groupName(g.getGroupName())
                .groupComment(g.getGroupComment())
                .groupImage(g.getGroupImage())
                .groupMaxCount(g.getGroupCount())          // ✅ groupCount → groupMaxCount
                .currentCount(g.getCurrentCount())
                .createdAt(g.getCreatedAt() == null ? null : g.getCreatedAt().toString())
                .updatedAt(g.getUpdatedAt() == null ? null : g.getUpdatedAt().toString())
                .build();
    }
}