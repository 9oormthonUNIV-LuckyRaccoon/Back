// src/main/java/luckkraccoon/family_memory/domain/question/dto/response/QuestionDetailResponse.java
package luckkraccoon.family_memory.domain.question.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailResponse {
    private Long id;
    private String questionName;
    private String questionComment;
    private Long chapterId;      // 소속 챕터 PK
    private Long indexId;        // 소속 목차 PK (nullable 가능)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}