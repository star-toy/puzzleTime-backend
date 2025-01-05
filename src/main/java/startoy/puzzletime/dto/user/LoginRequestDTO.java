package startoy.puzzletime.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotNull
    private String email;        // 사용자 이메일
    @NotNull
    private String name;         // 사용자 이름
    @NotNull
    private String provider;     // OAuth 제공자 (google 등)
    @NotNull
    private String providerId;   // 고유 사용자 ID
    @NotNull
    private String refreshToken; // Google Refresh Token
    private String appAccessToken; // 애플리케이션 Access Token
}
