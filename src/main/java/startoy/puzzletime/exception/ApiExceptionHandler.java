package startoy.puzzletime.exception;

// 예외 처리 핸들러

import org.springframework.http.HttpStatus;
import startoy.puzzletime.dto.ErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startoy.puzzletime.factory.ErrorResponseFactory;

@RestControllerAdvice
public class ApiExceptionHandler {

    // CustomException을 처리하는 핸들러 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // ErrorResponse 생성 및 상태 코드와 함께 반환
        ErrorResponse errorResponse = ErrorResponseFactory.createErrorResponse(
                errorCode.getMessage(),
                errorCode.getHttpStatus().value()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }

    // 그 외 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponseFactory.createErrorResponse("Internal Server Error", 500);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
