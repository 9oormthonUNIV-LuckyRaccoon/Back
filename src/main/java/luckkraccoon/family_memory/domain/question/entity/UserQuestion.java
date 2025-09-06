package luckkraccoon.family_memory.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;
import luckkraccoon.family_memory.domain.user.entity.User;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "user_questions",
        indexes = {
                @Index(name = "idx_user_question_user_id", columnList = "user_id"),
                @Index(name = "idx_user_question_question_id", columnList = "question_id")
        }
)
public class UserQuestion extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "answer_voice", length = 255)
    private String answerVoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
