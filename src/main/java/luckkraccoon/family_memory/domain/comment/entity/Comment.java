package luckkraccoon.family_memory.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.chapter.entity.UserChapter;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comment_user_chapter_id", columnList = "user_chapter_id")
        }
)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_chapter_id", nullable = false)
    private UserChapter userChapter;
}
