package luckkraccoon.family_memory.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private Long id;
    private String userId;
    private String nickName;
    private String email;
    private String gender;     // "MALE" / "FEMALE" / null
    private String birth;      // yyyy-MM-dd / null
    private String userImage;  // URL / null
    private Integer fontSize;  // null 가능
    private Integer voiceSpeed;// null 가능
    private Long groupId;      // null 가능
}