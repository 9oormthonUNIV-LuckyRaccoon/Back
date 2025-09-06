package luckkraccoon.family_memory.domain.user.service;

import lombok.RequiredArgsConstructor;
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
}