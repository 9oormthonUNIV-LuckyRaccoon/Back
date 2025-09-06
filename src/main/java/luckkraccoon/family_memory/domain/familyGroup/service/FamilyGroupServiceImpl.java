package luckkraccoon.family_memory.domain.familyGroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import luckkraccoon.family_memory.domain.familyGroup.converter.FamilyGroupConverter;
import luckkraccoon.family_memory.domain.familyGroup.dto.request.FamilyGroupCreateRequest;
import luckkraccoon.family_memory.domain.familyGroup.dto.response.FamilyGroupCreateResponse;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import luckkraccoon.family_memory.domain.familyGroup.repository.FamilyGroupRepository;
import luckkraccoon.family_memory.domain.user.entity.User;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;

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
}