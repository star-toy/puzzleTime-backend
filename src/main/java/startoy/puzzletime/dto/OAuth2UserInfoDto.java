package startoy.puzzletime.dto;

//OAuth2 사용자 정보 DTO. 추후 사용자가 필요에 따라 확장할 수 있습니다.
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2UserInfoDto {
    private String name;
    private String email;
    private String provider;
    private String providerId;
}
