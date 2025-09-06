package luckkraccoon.family_memory.domain.familyGroup.service;

import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupCreateRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupCreateResponse;

public interface FamilyGroupService {
    FamilyGroupCreateResponse create(Long ownerUserId, FamilyGroupCreateRequest request);
}