package luckkraccoon.family_memory.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import luckkraccoon.family_memory.domain.model.enums.Gender;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null은 “미변경”
public class UserUpdateRequest {
    private String userName;
    private String nickName;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private Gender gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String userImage;

    @Min(8)  @Max(40)
    private Integer fontSize;

    @Min(0)  @Max(3)
    private Integer voiceSpeed;
}