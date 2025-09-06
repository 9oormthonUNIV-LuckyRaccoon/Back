package luckkraccoon.family_memory.domain.chapter.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "chapter_indexes",
        indexes = {
                @Index(name = "idx_chapter_index_chapter_id", columnList = "chapter_id")
        }
)
public class ChapterIndex extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_name", nullable = false, length = 100)
    private String indexName;

    @Column(name = "index_comment", columnDefinition = "TEXT")
    private String indexComment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;
}
