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
@Table(name = "chapters")
public class Chapter extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_name", nullable = false, length = 100)
    private String chapterName;

    @Column(name = "chapter_comment", columnDefinition = "TEXT")
    private String chapterComment;
}
