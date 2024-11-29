package startoy.puzzletime.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import startoy.puzzletime.domain.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {

    private String message; // 응답 메시지
    @NotNull
    private User user;    // 사용자 정보
    @NotNull
    private String appAccessToken; // 토큰 값
    private boolean isNewUser;  // 신규 회원 여부 플래그 추가
}