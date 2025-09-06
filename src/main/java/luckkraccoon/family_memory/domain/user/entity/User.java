package luckkraccoon.family_memory.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import luckkraccoon.family_memory.domain.familyGroup.entity.FamilyGroup;
import luckkraccoon.family_memory.domain.model.entity.BaseEntity;
import luckkraccoon.family_memory.domain.model.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "uk_user_user_id", columnList = "user_id", unique = true),
                @Index(name = "uk_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_group_id", columnList = "group_id")
        }
)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인용 아이디 (UNIQUE)
    @Column(name = "user_id", nullable = false, length = 50, unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "nick_name", length = 50)
    private String nickName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "user_image", length = 255)
    private String userImage;

    @Column(name = "font_size")
    private Integer fontSize;

    @Column(name = "voice_speed")
    private Integer voiceSpeed;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)   // ✅ true
    @JoinColumn(name = "group_id", nullable = true)       // ✅ true
    private FamilyGroup familyGroup;
}
