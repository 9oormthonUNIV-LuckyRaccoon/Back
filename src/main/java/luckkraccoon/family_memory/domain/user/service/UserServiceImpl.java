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
import luckkraccoon.family_memory.global.error.code.status.ErrorStatus;
import luckkraccoon.family_memory.global.exception.GeneralException;
import luckkraccoon.family_memory.domain.model.service.S3UploadService;

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

        // 중복 검증
        if (userRepository.existsByUserId(request.getUserId()) ||
                userRepository.existsByEmail(request.getEmail())) {
            // 명세서: 400 (이미 사용 중인 아이디 또는 이메일)
            throw new GeneralException(ErrorStatus._BAD_REQUEST, "이미 사용 중인 아이디 또는 이메일입니다.");
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(request.getUserPassword());

        // 이미지 업로드 결정: 파일 우선, 없으면 요청의 URL 사용
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            // 버킷 하위 디렉터리는 프로젝트 정책에 맞게 변경
            // s3UploadService.uploadFile(...) 시그니처는 프로젝트 코드에 맞게 사용
            imageUrl = s3UploadService.uploadFile(imageFile, "profile");
        } else {
            imageUrl = request.getUserImage();
        }

        // 엔티티 생성/적용
        User user = new User();
        UserConverter.applySignupFields(user, request, encodedPassword, imageUrl);

        userRepository.save(user);

        return UserConverter.toSignupResponse(user);
    }
}
