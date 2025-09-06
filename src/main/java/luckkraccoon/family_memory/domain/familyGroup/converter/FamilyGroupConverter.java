package luckkraccoon.family_memory.domain.familyGroup.converter;

import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupCreateResponse;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupGetResponse;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupMembersResponse;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import luckkraccoon.family_memory.domain.user.entity.User;

import java.util.List;

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

    public static FamilyGroupMembersResponse toMembersResponse(
            FamilyGroup group,
            List<User> users,
            int currentCount
    ) {
        List<FamilyGroupMembersResponse.Member> members = users.stream()
                .map(u -> FamilyGroupMembersResponse.Member.builder()
                        .id(u.getId())
                        .userId(u.getUserId())
                        .nickName(u.getNickName())
                        .userName(u.getUserName())
                        .userImage(u.getUserImage())
                        .gender(u.getGender() == null ? null : u.getGender().name())
                        .birth(u.getBirth() == null ? null : u.getBirth().toString())
                        .createdAt(u.getCreatedAt() == null ? null : u.getCreatedAt().toString())
                        .build())
                .toList();

        return FamilyGroupMembersResponse.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .currentCount(currentCount)
                .members(members)
                .build();
    }

}