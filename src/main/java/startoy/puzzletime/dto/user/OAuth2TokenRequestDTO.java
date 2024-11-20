package startoy.puzzletime.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for OAuth2 Token Request
 */
@Getter
@Setter

public class OAuth2TokenRequestDTO {

    @NotNull
    @Schema(description = "Authorization Code", example = "4/0AX4XfW...")
    private String code;

    @NotNull
    @Schema(description = "Google Client ID", example = "YOUR_CLIENT_ID")
    private String clientId;

    @NotNull
    @Schema(description = "Google Client Secret", example = "YOUR_CLIENT_SECRET")
    private String clientSecret;

    @NotNull
    @Schema(description = "Redirect URI", example = "http://localhost:9090/login/oauth2/code/google")
    private String redirectUri;

    // 변경 X
    @Schema(description = "Grant Type", example = "authorization_code")
    private String grantType;
}
