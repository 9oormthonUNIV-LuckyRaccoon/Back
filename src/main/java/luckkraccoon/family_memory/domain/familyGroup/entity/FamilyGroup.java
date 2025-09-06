package luckkraccoon.family_memory.domain.familyGroup.entity;

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
        name = "family_groups",
        indexes = {
                @Index(name = "uk_family_group_join_id", columnList = "group_join_id", unique = true)
        }
)
public class FamilyGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_join_id", nullable = false, length = 100, unique = true)
    private String groupJoinId;

    @Column(name = "group_password", nullable = false, length = 100)
    private String groupPassword;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(name = "group_comment", columnDefinition = "TEXT")
    private String groupComment;

    @Column(name = "group_image", length = 255)
    private String groupImage;

    // 최대 인원 수
    @Column(name = "group_count", nullable = false)
    private Integer groupCount;

    // 현재 인원 수
    @Column(name = "current_count", nullable = false)
    private Integer currentCount;
}
