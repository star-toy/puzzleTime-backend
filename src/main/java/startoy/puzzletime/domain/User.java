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
    @Column(name = "username", nullable = false)
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
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken; // Google Refresh Token 값

    @NotNull
    @Column(name = "app_access_token", columnDefinition = "TEXT")
    private String appAccessToken; // 애플리케이션 Access Token

    @Column(name = "app_token_expires_at")
    private LocalDateTime appTokenExpiresAt; // 애플리케이션 토큰 만료일자

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}