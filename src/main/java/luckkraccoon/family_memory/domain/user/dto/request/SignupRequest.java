package luckkraccoon.family_memory.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luckkraccoon.family_memory.domain.model.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "userId는 필수입니다.")
    @Size(min = 4, max = 20, message = "userId는 4~20자입니다.")
    private String userId;

    @NotBlank(message = "userPassword는 필수입니다.")
    @Size(min = 8, max = 64, message = "비밀번호는 8~64자입니다.")
    private String userPassword;

    @NotBlank(message = "userName은 필수입니다.")
    @Size(max = 30)
    private String userName;

    @NotBlank(message = "nickName은 필수입니다.")
    @Size(max = 30)
    private String nickName;

    @NotBlank(message = "email은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "gender는 필수입니다.")
    private Gender gender;

    @NotNull(message = "birth는 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;

    /** 파일 미전송 시 URL 직접 입력에 사용(선택) */
    private String userImage;

    /** 선택값 */
    @Min(8) @Max(40)
    private Integer fontSize;

    /** 선택값: 음성 속도(앱 정책에 맞게 조정) */
    @Min(0) @Max(3)
    private Integer voiceSpeed;
}
