package luckkraccoon.family_memory.domain.familyGroup.service;

import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupJoinRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupLeaveRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupUpdateRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import luckkraccoon.family_memory.domain.familyGroup.converter.FamilyGroupConverter;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupCreateRequest;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import luckkraccoon.family_memory.domain.familyGroup.repository.FamilyGroupRepository;
import luckkraccoon.family_memory.domain.user.entity.User;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FamilyGroupServiceImpl implements FamilyGroupService {

    private final FamilyGroupRepository familyGroupRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public FamilyGroupCreateResponse create(Long ownerUserId, FamilyGroupCreateRequest req) {
        // 1) 비밀번호 확인
        if (!req.getGroupPassword().equals(req.getGroupPasswordCheck())) {
            throw new UserHandler(ErrorStatus._BAD_REQUEST); // 400: "요청 값이 유효하지 않습니다."
        }

        // 2) groupJoinId 중복 체크
        if (familyGroupRepository.existsByGroupJoinId(req.getGroupJoinId())) {
            throw new UserHandler(ErrorStatus.CONFLICT); // 409
        }

        // 3) 생성자 조회
        User owner = userRepository.findById(ownerUserId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404

        // 4) 그룹 엔티티 생성
        String hashed = passwordEncoder.encode(req.getGroupPassword());

        FamilyGroup group = FamilyGroup.builder()
                .groupJoinId(req.getGroupJoinId())
                .groupPassword(hashed)           // 평문 저장 금지
                .groupName(req.getGroupName())
                .groupComment(req.getGroupComment())
                .groupImage(req.getGroupImage())
                .groupCount(req.getGroupMaxCount())
                .currentCount(1)                 // 생성자 자동 참여
                .build();

        FamilyGroup saved = familyGroupRepository.save(group);

        // 5) 생성자를 그룹에 연결(User.familyGroup = createdGroup)
        owner.setFamilyGroup(saved); // users.group_id 업데이트
        // (필요 시 userRepository.save(owner); 가 없어도 트랜잭션 종료 시 dirty checking 반영)

        // 6) 응답
        return FamilyGroupConverter.toCreateResponse(saved, owner.getId());
    }

    @Override
    @Transactional
    public FamilyGroupJoinResponse join(Long userId, FamilyGroupJoinRequest req) {
        // 1) 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404: 사용자 없음

        // 단일 소속 정책: 이미 다른 그룹에 속해 있으면 403
        if (user.getFamilyGroup() != null) {
            // 같은 그룹이면 409 (이미 참여)
            if (user.getFamilyGroup().getGroupJoinId().equals(req.getGroupJoinId())) {
                throw new UserHandler(ErrorStatus.CONFLICT); // 409: 이미 해당 그룹에 참여되어 있습니다.
            }
            throw new UserHandler(ErrorStatus.FORBIDDEN); // 403: 이미 다른 그룹에 소속
        }

        // 2) 그룹 조회 (행 잠금으로 동시성 제어)
        FamilyGroup group = familyGroupRepository.findForUpdateByGroupJoinId(req.getGroupJoinId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404: 그룹 없음

        // 3) 비밀번호 검증 (저장은 해시, 비교는 matches)
        if (!passwordEncoder.matches(req.getGroupPassword(), group.getGroupPassword())) {
            throw new UserHandler(ErrorStatus.CONFLICT); // 409: 비밀번호 불일치
        }

        // 4) 정원 체크 (currentCount < groupCount)
        Integer max = group.getGroupCount();      // 엔티티가 groupCount 라고 가정
        Integer curr = group.getCurrentCount() == null ? 0 : group.getCurrentCount();
        if (max != null && curr >= max) {
            throw new UserHandler(ErrorStatus.CONFLICT); // 409: 정원 초과
        }

        // 5) 참여 처리
        group.setCurrentCount(curr + 1);
        user.setFamilyGroup(group); // users.group_id 업데이트

        // 6) 응답
        return FamilyGroupJoinResponse.builder()
                .userId(user.getId())
                .groupId(group.getId())
                .groupJoinId(group.getGroupJoinId())
                .groupName(group.getGroupName())
                .currentCount(group.getCurrentCount())
                .groupMaxCount(group.getGroupCount())
                .joinedAt(Instant.now().toString()) // ISO-8601 with 'Z'
                .build();
    }

    @Override
    @Transactional
    public FamilyGroupLeaveResponse leave(FamilyGroupLeaveRequest req) {
        // 1) 사용자 확인
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404

        // 2) 그룹 확인 + 행 잠금
        FamilyGroup group = familyGroupRepository.findForUpdateById(req.getGroupId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404

        // 3) 소속 일치 검증
        if (user.getFamilyGroup() == null || !user.getFamilyGroup().getId().equals(group.getId())) {
            // 명세: 409 "해당 사용자는 이 그룹에 소속되어 있지 않습니다."
            throw new UserHandler(ErrorStatus.CONFLICT);
        }

        // 4) 탈퇴 처리
        Integer curr = group.getCurrentCount() == null ? 0 : group.getCurrentCount();
        group.setCurrentCount(Math.max(0, curr - 1)); // 아래로 떨어지지 않게

        user.setFamilyGroup(null); // users.group_id = NULL

        // 5) 응답
        return FamilyGroupLeaveResponse.builder()
                .userId(user.getId())
                .groupId(group.getId())
                .currentCount(group.getCurrentCount())
                .leftAt(Instant.now().toString()) // ISO-8601 with 'Z'
                .build();
    }

    @Override
    public FamilyGroupGetResponse getById(Long id) {
        FamilyGroup group = familyGroupRepository.findById(id)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404: 그룹 없음
        return FamilyGroupConverter.toGetResponse(group);
    }

    @Override
    public FamilyGroupMembersResponse getMembers(Long groupId, String q) {
        // 1) 그룹 존재 확인
        FamilyGroup group = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // "그룹을 찾을 수 없습니다."

        // 2) 구성원 조회 (q 없으면 전체, 있으면 LIKE 검색)
        final List<User> users = (q == null || q.isBlank())
                ? userRepository.findByFamilyGroup_Id(groupId)
                : userRepository.searchMembersList(groupId, q.trim());

        // 3) 서버 기준 현재 인원 수
        int currentCount = (int) userRepository.countByFamilyGroup_Id(groupId);

        // 4) Converter로 응답 변환
        return FamilyGroupConverter.toMembersResponse(group, users, currentCount);
    }

    @Override
    @Transactional
    public FamilyGroupUpdateResponse updateGroup(Long groupId, FamilyGroupUpdateRequest req) {
        FamilyGroup group = familyGroupRepository.findById(groupId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // "그룹을 찾을 수 없습니다."

        // 현재 인원은 서버 기준(실시간)으로 계산
        int currentCount = (int) userRepository.countByFamilyGroup_Id(groupId);

        // 부분 업데이트
        if (req.getGroupName() != null)    group.setGroupName(req.getGroupName());
        if (req.getGroupComment() != null) group.setGroupComment(req.getGroupComment());
        if (req.getGroupImage() != null)   group.setGroupImage(req.getGroupImage()); // "" 허용

        if (req.getGroupMaxCount() != null) {
            int newMax = req.getGroupMaxCount();
            if (newMax < currentCount) {
                throw new UserHandler(ErrorStatus.FAMILY_GROUP_MAXCOUNT_TOO_SMALL); // 409
            }
            group.setGroupCount(newMax);
        }

        // JPA flush는 트랜잭션 종료 시
        return FamilyGroupConverter.toUpdateResponse(group, currentCount);
    }

}