package luckkraccoon.family_memory.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponse {
    private Long id;
    private String userId;
    private String nickName;
    private String email;
}