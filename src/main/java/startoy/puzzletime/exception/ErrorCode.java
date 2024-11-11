package startoy.puzzletime.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 아트웍 관련 에러
    ARTWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "Artwork not found"),

    // 퍼즐 관련 에러
    PUZZLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Puzzle not found"),

    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

    // 기타 일반적인 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus httpStatus;
    private final String message;

    // 생성자: 상태 코드와 메시지를 전달받아 설정
    ErrorCode(HttpStatus httpStatus, String message) {
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
