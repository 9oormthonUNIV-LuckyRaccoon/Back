package luckkraccoon.family_memory.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.chapter.entity.ChapterIndex;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "questions",
        indexes = {
                @Index(name = "idx_question_index_id", columnList = "index_id")
        }
)
public class Question extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_name", nullable = false, length = 255)
    private String questionName;

    @Column(name = "question_comment", columnDefinition = "TEXT")
    private String questionComment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "index_id", nullable = false)
    private ChapterIndex index;
}
