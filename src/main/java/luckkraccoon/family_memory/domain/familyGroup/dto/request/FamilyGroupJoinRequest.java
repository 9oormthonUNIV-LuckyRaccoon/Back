package luckkraccoon.family_memory.domain.familyGroup.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FamilyGroupJoinRequest {

    @NotBlank(message = "groupJoinId는 필수 값입니다.")
    private String groupJoinId;

    @NotBlank(message = "groupPassword는 필수 값입니다.")
    private String groupPassword;
}