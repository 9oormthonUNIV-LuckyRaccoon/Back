package luckkraccoon.family_memory.domain.familyGroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupJoinRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupLeaveRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupGetResponse;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupJoinResponse;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupLeaveResponse;
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


    @Operation(summary = "가족 그룹 참여", description = "초대 ID/비밀번호로 그룹에 참여합니다. (무인증)")
    @PostMapping(value = "/family-group/join", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<FamilyGroupJoinResponse>> joinFamilyGroup(
            @RequestParam("userId") Long userId,            // 무인증 환경에서 참가자 식별
            @Valid @RequestBody FamilyGroupJoinRequest request
    ) {
        FamilyGroupJoinResponse result = familyGroupService.join(userId, request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.FAMILY_GROUP_JOIN_SUCCESS, result)
        );
    }

    @Operation(summary = "가족 그룹 탈퇴", description = "사용자를 해당 가족 그룹에서 탈퇴시킵니다. (무인증)")
    @PostMapping(value = "/family-group/leave", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<FamilyGroupLeaveResponse>> leaveFamilyGroup(
            @Valid @RequestBody FamilyGroupLeaveRequest request
    ) {
        FamilyGroupLeaveResponse result = familyGroupService.leave(request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.FAMILY_GROUP_LEAVE_SUCCESS, result)
        );
    }


    @Operation(summary = "가족 그룹 정보 조회", description = "가족 그룹의 기본 정보를 조회합니다. (무인증)")
    @GetMapping(value = "/family-group/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<FamilyGroupGetResponse>> getFamilyGroup(@PathVariable("id") Long id) {
        FamilyGroupGetResponse result = familyGroupService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.FAMILY_GROUP_GET_SUCCESS, result)
        );
    }
}