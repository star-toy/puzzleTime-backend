package startoy.puzzletime.exception;

// 예외 처리 핸들러

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import startoy.puzzletime.controller.AuthController;
import startoy.puzzletime.dto.ErrorResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startoy.puzzletime.factory.ErrorResponseFactory;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    // CustomException을 처리하는 핸들러 메서드
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        // 토큰 만료 관련 예외 처리
        /*if (errorCode == ErrorCode.TOKEN_EXPIRED) {
            ErrorResponseDTO errorResponseDTO = ErrorResponseFactory.createErrorResponse(
                    "Token has expired. Please log in again.",
                    HttpStatus.UNAUTHORIZED.value()
            );
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNAUTHORIZED);
        }*/

        // ErrorResponse 생성 및 상태 코드와 함께 반환
        ErrorResponseDTO errorResponseDTO = ErrorResponseFactory.createErrorResponse(
                errorCode.getCode(),// 고유 코드
                errorCode.getMessage(),
                errorCode.getHttpStatus().value()
        );
        return new ResponseEntity<>(errorResponseDTO, errorCode.getHttpStatus());
    }

    // 그 외 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        // 예외 메시지 로깅
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);

        // 예상치 못한 예외의 기본 응답
        ErrorResponseDTO errorResponseDTO = ErrorResponseFactory.createErrorResponse(
                "ERR_INTERNAL_SERVER_ERROR", // 고유 에러 코드
                "An unexpected error occurred. Please contact support.", // 사용자 친화적인 메시지
                500 // 상태 코드
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
