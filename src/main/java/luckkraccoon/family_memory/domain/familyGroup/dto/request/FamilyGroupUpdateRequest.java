package luckkraccoon.family_memory.domain.familyGroup.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FamilyGroupUpdateRequest {
    private String groupName;        // null이면 미변경
    private String groupComment;     // null이면 미변경
    private String groupImage;       // null이면 미변경 ("" 주면 비움)
    @Min(1)
    private Integer groupMaxCount;   // null이면 미변경, 값 있으면 currentCount 이상이어야 함
}