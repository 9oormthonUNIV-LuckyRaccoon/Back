package luckkraccoon.family_memory.domain.question.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnswerCreateRequest {
    @NotNull
    private Long userId;

    // 엔티티는 answer 필드지만, 요청 명은 content로 받는다
    @NotBlank
    private String content;

    private Boolean overwrite; // 기본 false
}