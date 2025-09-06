package luckkraccoon.family_memory.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDeleteResponse {
    private Long id;
    private String userId;
    private boolean deleted;
}