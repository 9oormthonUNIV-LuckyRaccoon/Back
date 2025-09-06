package luckkraccoon.family_memory.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGetResponse {
    private Long id;
    private String userId;
    private String userName;
    private String nickName;
    private String email;
    private String gender;     // "MALE" / "FEMALE" / null
    private String birth;      // yyyy-MM-dd / null
    private String userImage;  // URL / null
    private Integer fontSize;  // null 가능
    private Integer voiceSpeed;// null 가능
    private Long groupId;      // null 가능
    private String createdAt;  // ISO-8601 (LocalDateTime.toString)
    private String updatedAt;  // ISO-8601
}