package luckkraccoon.family_memory.domain.familyGroup.service;

import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupCreateRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupJoinRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupLeaveRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.*;

public interface FamilyGroupService {
    FamilyGroupCreateResponse create(Long ownerUserId, FamilyGroupCreateRequest request);
    FamilyGroupJoinResponse join(Long userId, FamilyGroupJoinRequest request);
    FamilyGroupLeaveResponse leave(FamilyGroupLeaveRequest request);
    FamilyGroupGetResponse getById(Long id);
    FamilyGroupMembersResponse getMembers(Long groupId, String q);  // page/size 미지원

}