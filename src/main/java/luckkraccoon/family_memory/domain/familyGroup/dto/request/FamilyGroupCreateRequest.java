package luckkraccoon.family_memory.domain.familyGroup.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FamilyGroupCreateRequest {

    @NotBlank(message = "groupJoinId는 필수입니다.")
    private String groupJoinId;

    @NotBlank(message = "groupPassword는 필수입니다.")
    private String groupPassword;

    @NotBlank(message = "groupPasswordCheck는 필수입니다.")
    private String groupPasswordCheck;

    @NotBlank(message = "groupName은 필수입니다.")
    private String groupName;

    private String groupComment; // 선택
    private String groupImage;   // 선택

    @NotNull(message = "groupMaxCount는 필수입니다.")
    @Min(value = 1, message = "groupMaxCount는 1 이상이어야 합니다.")
    private Integer groupMaxCount;   // ERD에서 groupCount와 동일 개념
}