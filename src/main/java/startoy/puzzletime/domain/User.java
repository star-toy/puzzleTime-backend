package startoy.puzzletime.domain;
//구글 OAuth 정보를 저장할 User 엔티티.

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_uid", unique = true, nullable = false, length = 36)
    private String userUid;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // google
    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}