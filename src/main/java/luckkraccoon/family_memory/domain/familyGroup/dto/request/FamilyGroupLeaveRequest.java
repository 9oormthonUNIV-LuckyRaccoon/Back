package luckkraccoon.family_memory.domain.familyGroup.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FamilyGroupLeaveRequest {

    @NotNull(message = "userId는 필수 값입니다.")
    private Long userId;

    @NotNull(message = "groupId는 필수 값입니다.")
    private Long groupId;
}