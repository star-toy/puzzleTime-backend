package startoy.puzzletime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 테마 관련 에러 THEME_NOT_FOUND
    THEME_NOT_FOUND("ERR_THEME_NOT_FOUND",HttpStatus.NOT_FOUND, "Theme not found"),

    // 아트웍 관련 에러
    ARTWORK_NOT_FOUND("ERR_ARTWORK_NOT_FOUND", HttpStatus.NOT_FOUND, "Artwork not found"),

    // 퍼즐 관련 에러
    PUZZLE_NOT_FOUND("ERR_PUZZLE_NOT_FOUND", HttpStatus.NOT_FOUND, "Puzzle not found"),

    // 사용자 관련 에러
    USER_NOT_FOUND("ERR_USER_NOT_FOUND", HttpStatus.UNAUTHORIZED, "User not found"),

    // 이미지 관련 에러
    IMAGE_NOT_FOUND("ERR_IMAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Image not found"),

    // 토큰 관련 에러
    INVALID_TOKEN("ERR_INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "No token found in cookie"),
    TOKEN_REISSUE_REQUIRED("ERR_TOKEN_REISSUE_REQUIRED", HttpStatus.UNAUTHORIZED, "The token is Invalid. Please reauthenticate to obtain a new token."),
    TOKEN_EXPIRED("ERR_TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "Token has expired"),
    TOKEN_NOT_FOUND("ERR_TOKEN_NOT_FOUND", HttpStatus.UNAUTHORIZED, "Token not found"),
    TOKEN_MISSING("ERR_TOKEN_MISSING", HttpStatus.BAD_REQUEST, "Token is missing."),

    // 기타 일반적인 에러
    BAD_REQUEST("ERR_BAD_REQUEST", HttpStatus.BAD_REQUEST, "Bad request"),
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");


    private final String code; // 고유 에러 코드
    private final HttpStatus httpStatus;
    private final String message;

    // 생성자: 상태 코드와 메시지를 전달받아 설정
    ErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    }