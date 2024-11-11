package startoy.puzzletime.exception;

// 예외 처리 핸들러

import org.springframework.http.HttpStatus;
import startoy.puzzletime.dto.ErrorResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startoy.puzzletime.factory.ErrorResponseFactory;

@RestControllerAdvice
public class ApiExceptionHandler {

    // CustomException을 처리하는 핸들러 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // ErrorResponse 생성 및 상태 코드와 함께 반환
        ErrorResponseDTO errorResponseDTO = ErrorResponseFactory.createErrorResponse(
                errorCode.getMessage(),
                errorCode.getHttpStatus().value()
        );
        return new ResponseEntity<>(errorResponseDTO, errorCode.getHttpStatus());
    }

    // 그 외 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        ErrorResponseDTO errorResponseDTO = ErrorResponseFactory.createErrorResponse("Internal Server Error", 500);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
