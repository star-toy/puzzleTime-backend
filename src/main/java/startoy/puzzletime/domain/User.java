package startoy.puzzletime.domain;
//구글 OAuth 정보를 저장할 User 엔티티.

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @NotNull
    private Long id;

    @NotNull
    @Column(name = "user_uid", unique = true, nullable = false, length = 36)
    private String userUid;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // google
    @Column(name = "provider")
    private String provider;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}