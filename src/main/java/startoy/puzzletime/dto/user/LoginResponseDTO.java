package startoy.puzzletime.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private String message; // 응답 메시지
    @NotNull
    private Object user;    // 사용자 정보
    @NotNull
    private String accessToken; // 토큰 값
}