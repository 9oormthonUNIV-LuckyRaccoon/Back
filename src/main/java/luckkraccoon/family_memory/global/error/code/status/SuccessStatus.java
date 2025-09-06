package luckkraccoon.family_memory.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 멤버
    LOGIN_SUCCESS(HttpStatus.OK, "MEMBER_SE2001", "회원 로그인이 성공되었습니다."),
    SIGNUP_SUCCESS(org.springframework.http.HttpStatus.OK, "MEMBER_SE2002", "회원가입 성공"),
    PROFILE_SETUP_SUCCESS(HttpStatus.OK, "MEMBER_SE2003", "프로필 설정 완료"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "200", "회원정보가 수정되었습니다."),
    USER_GET_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "회원정보 조회 성공"),
    USER_DELETE_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "회원 탈퇴가 완료되었습니다."),
    FAMILY_GROUP_CREATE_SUCCESS(org.springframework.http.HttpStatus.CREATED, "201", "가족 그룹이 생성되었습니다."),
    FAMILY_GROUP_JOIN_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "가족 그룹에 참여했습니다."),
    FAMILY_GROUP_LEAVE_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "가족 그룹에서 탈퇴했습니다."),
    FAMILY_GROUP_GET_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "가족 그룹 정보 조회 성공"),
    FAMILY_GROUP_MEMBERS_GET_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "구성원 목록 조회 성공"),
    FAMILY_GROUP_UPDATE_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "가족 그룹 정보가 수정되었습니다."),
    CHAPTER_LIST_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "챕터 목록 조회 성공"),
    CHAPTER_LIST_EMPTY(org.springframework.http.HttpStatus.NO_CONTENT, "204", "조회 가능한 챕터가 없습니다."),
    USERCHAPTER_GET_SUCCESS(org.springframework.http.HttpStatus.OK, "200", "사용자챕터 조회 성공."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
