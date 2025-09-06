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
            summary = "회원가입(JSON)",
            description = "요청 본문(JSON)으로 회원가입을 수행합니다. 이미지 URL을 직접 전달할 수 있습니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signupJson(@Valid @RequestBody SignupRequest request) {
        SignupResponse result = userService.signup(request, null);
        // 명세서: 201 Created
        return ResponseEntity.status(201).body(ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, result));
    }

    @Operation(
            summary = "회원가입(Multipart)",
            description = "multipart/form-data로 회원가입을 수행합니다. imageFile이 있으면 S3 업로드 후 URL을 사용합니다."
    )
    @PostMapping(value = "/signup-multipart", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<SignupResponse>> signupMultipart(
            @RequestPart("request") @Valid SignupRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        SignupResponse result = userService.signup(request, imageFile);
        return ResponseEntity.status(201).body(ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, result));
    }
}
