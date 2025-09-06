package luckkraccoon.family_memory.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponse {
    private Long id;
    private String userId;     // 불변
    private String userName;
    private String nickName;
    private String email;
    private String gender;     // "MALE"/"FEMALE"/null
    private String birth;      // yyyy-MM-dd / null
    private String userImage;  // URL / null
    private Integer fontSize;  // null 가능
    private Integer voiceSpeed;// null 가능
    private String updatedAt;  // ISO-8601(LocalDateTime.toString)
}