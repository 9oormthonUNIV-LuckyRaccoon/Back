package luckkraccoon.family_memory.domain.familyGroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupCreateRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupCreateResponse;
import luckkraccoon.family_memory.domain.familyGroup.service.FamilyGroupService;
import luckkraccoon.family_memory.global.common.response.ApiResponse;
import luckkraccoon.family_memory.global.error.code.status.SuccessStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "FamilyGroup", description = "가족 그룹 API")
public class FamilyGroupController {

    private final FamilyGroupService familyGroupService;

    @Operation(summary = "가족 그룹 생성", description = "고유 초대 ID와 비밀번호로 새 그룹을 생성하고 생성자를 자동 참여시킵니다. (무인증)")
    @PostMapping(value = "/family-group", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<FamilyGroupCreateResponse>> createFamilyGroup(
            @RequestParam("ownerUserId") Long ownerUserId,   // 무인증 환경에서 생성자 식별을 위해 필요
            @Valid @RequestBody FamilyGroupCreateRequest request
    ) {
        FamilyGroupCreateResponse result = familyGroupService.create(ownerUserId, request);
        return ResponseEntity.status(201)
                .body(ApiResponse.onSuccess(SuccessStatus.FAMILY_GROUP_CREATE_SUCCESS, result));
    }
}