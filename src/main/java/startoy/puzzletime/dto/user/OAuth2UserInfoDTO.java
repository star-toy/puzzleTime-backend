package startoy.puzzletime.dto.user;

//OAuth2 사용자 정보 DTO. 추후 사용자가 필요에 따라 확장할 수 있습니다.
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2UserInfoDTO {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String provider;
    @NotNull
    private String providerId;
}
