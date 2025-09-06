package luckkraccoon.family_memory.domain.chapter.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;
import luckkraccoon.family_memory.domain.model.enums.UserChapterState;
import luckkraccoon.family_memory.domain.question.entity.Question;
import luckkraccoon.family_memory.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "user_chapters",
        indexes = {
                @Index(name = "idx_user_chapter_user_id", columnList = "user_id"),
                @Index(name = "idx_user_chapter_chapter_id", columnList = "chapter_id"),
                @Index(name = "idx_user_chapter_last_question_id", columnList = "last_question_id")
        }
)
public class UserChapter extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20)
    private UserChapterState state; // NONE, PROGRESS, SUCCESS

    @Column(name = "progress_percent")
    private Integer progressPercent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_question_id")
    private Question lastQuestion;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}