package startoy.puzzletime.factory;

import startoy.puzzletime.dto.ErrorResponseDTO;
import java.time.LocalDateTime;

public class ErrorResponseFactory {

    // ErrorResponse를 생성하는 정적 팩토리 메서드
    public static ErrorResponseDTO createErrorResponse(String message, int status) {
        return new ErrorResponseDTO(message, status, LocalDateTime.now());
    }
}
