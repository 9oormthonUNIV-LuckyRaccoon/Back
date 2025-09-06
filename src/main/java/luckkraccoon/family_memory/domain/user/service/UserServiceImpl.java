package luckkraccoon.family_memory.domain.user.service;

import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.user.dto.request.LoginRequest;
import luckkraccoon.family_memory.domain.user.dto.request.UserUpdateRequest;
import luckkraccoon.family_memory.domain.user.dto.response.LoginResponse;
import luckkraccoon.family_memory.domain.user.dto.response.UserGetResponse;
import luckkraccoon.family_memory.domain.user.dto.response.UserUpdateResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
import luckkraccoon.family_memory.domain.user.dto.response.SignupResponse;
import luckkraccoon.family_memory.domain.user.entity.User;
import luckkraccoon.family_memory.domain.user.repository.UserRepository;
import luckkraccoon.family_memory.domain.user.converter.UserConverter;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;
import luckkraccoon.family_memory.domain.model.service.S3UploadService; // ✅ 패키지 경로 확인

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploadService s3UploadService;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request, MultipartFile imageFile) {

        // 파일 필수 (멀티파트 전용)
        if (imageFile == null || imageFile.isEmpty()) {
            throw new UserHandler(ErrorStatus._BAD_REQUEST);
        }

        // 중복 검증
        if (userRepository.existsByUserId(request.getUserId())
                || userRepository.existsByEmail(request.getEmail())) { // ✅ 괄호 정상
            // 전용 코드가 있으면 ErrorStatus.DUPLICATE_USER 권장
            throw new UserHandler(ErrorStatus._BAD_REQUEST);
        }

        // 비밀번호 인코딩
        final String encodedPassword = passwordEncoder.encode(request.getUserPassword());

        // S3 업로드
        final String imageUrl;
        try {
            imageUrl = s3UploadService.upload(imageFile, "profile");
        } catch (Exception e) { // upload() 가 throws 하면 여기서 변환
            // 전용 코드가 있으면 ErrorStatus.FILE_UPLOAD_FAIL 권장
            throw new UserHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

        // 엔티티 생성 (기본생성자 PROTECTED → 빌더 사용)
        User user = UserConverter.toSignupEntity(request, encodedPassword, imageUrl);

        userRepository.save(user);
        return UserConverter.toSignupResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1) 아이디로 조회 (존재/비밀번호 오류는 동일 메시지)
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.INVALID_CREDENTIALS)); // 400

        // 2) 비밀번호 검증
        if (!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_CREDENTIALS); // 400
        }

        // 3) 성공 → 사용자 정보만 반환 (토큰 없음)
        return UserConverter.toLoginResponse(user);
    }

    @Override
    @Transactional
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest req) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404

        // 이메일 변경 시 중복 검사 (자기 자신 제외)
        if (req.getEmail() != null) {
            String newEmail = req.getEmail();
            String currEmail = user.getEmail();
            boolean changed = (currEmail == null) || !currEmail.equalsIgnoreCase(newEmail);
            if (changed && userRepository.existsByEmail(newEmail)) {
                throw new UserHandler(ErrorStatus.CONFLICT); // 409
            }
        }

        // 부분 업데이트 적용 (null은 미변경)
        if (req.getUserName()  != null) user.setUserName(req.getUserName());
        if (req.getNickName()  != null) user.setNickName(req.getNickName());
        if (req.getEmail()     != null) user.setEmail(req.getEmail());
        if (req.getGender()    != null) user.setGender(req.getGender());
        if (req.getBirth()     != null) user.setBirth(req.getBirth());
        if (req.getUserImage() != null) user.setUserImage(req.getUserImage());
        if (req.getFontSize()  != null) user.setFontSize(req.getFontSize());
        if (req.getVoiceSpeed()!= null) user.setVoiceSpeed(req.getVoiceSpeed());

        // JPA Auditing 으로 updatedAt 자동 갱신
        // flush는 트랜잭션 끝에서

        return UserConverter.toUpdateResponse(user);
    }

    @Override
    public UserGetResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserHandler(ErrorStatus.NOT_FOUND)); // 404
        return UserConverter.toGetResponse(user);
    }

}