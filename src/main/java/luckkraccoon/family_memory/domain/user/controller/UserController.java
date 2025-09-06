package luckkraccoon.family_memory.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckkraccoon.family_memory.domain.user.dto.request.LoginRequest;
import luckkraccoon.family_memory.domain.user.dto.request.UserUpdateRequest;
import luckkraccoon.family_memory.domain.user.dto.response.*;
import luckkraccoon.family_memory.domain.user.handler.UserHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
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


    @Operation(summary = "로그인", description = "userId / userPassword로 로그인 (토큰 발급 없음)")
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse result = userService.login(request);
        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, result));
    }

    @Operation(summary = "회원정보 부분수정", description = "전달된 필드만 갱신 (id,userId 불변)")
    @PatchMapping(value = "/users/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {

        UserUpdateResponse result = userService.updateUser(id, request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.USER_UPDATE_SUCCESS, result)
        );
    }

    @Operation(summary = "회원정보 조회", description = "특정 사용자의 프로필/환경설정 반환 (무인증)")
    @GetMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUser(@PathVariable("id") Long id) {
        UserGetResponse result = userService.getUser(id);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.USER_GET_SUCCESS, result)
        );
    }

    @Operation(summary = "회원 탈퇴", description = "사용자를 하드 삭제합니다. (무인증)")
    @DeleteMapping(value = "/users/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> deleteUser(@PathVariable("id") Long id) {
        UserDeleteResponse result = userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus.USER_DELETE_SUCCESS, result)
        );
    }

}