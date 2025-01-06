package startoy.puzzletime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private final String code;    // 고유 에러 코드
    private final String message;        // 에러 메시지
    private final int status;            // HTTP 상태 코드
    private final LocalDateTime timestamp;  // 에러 발생 시간
}
