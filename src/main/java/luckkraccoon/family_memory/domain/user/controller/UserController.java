package luckkraccoon.family_memory.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
import luckkraccoon.family_memory.domain.user.dto.response.SignupResponse;
import luckkraccoon.family_memory.domain.user.service.UserService;
import luckkraccoon.family_memory.global.common.response.ApiResponse;
import luckkraccoon.family_memory.global.error.code.status.SuccessStatus;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입(Multipart)",
            description = "multipart/form-data로 회원가입. 이미지 파일은 필수이며 S3에 업로드 후 URL 저장."
    )
    @PostMapping(value = "/signup-multipart", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<SignupResponse>> signupMultipart(
            @RequestPart("request") @Valid SignupRequest request,     // ✅ userImage 없음
            @RequestPart("imageFile") MultipartFile imageFile         // ✅ 파일 필수
    ) {
        SignupResponse result = userService.signup(request, imageFile);
        return ResponseEntity.status(201)
                .body(ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, result));
    }
}