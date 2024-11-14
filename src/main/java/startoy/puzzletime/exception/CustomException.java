package startoy.puzzletime.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    // ErrorCode를 반환하는 getter
    private final ErrorCode errorCode;

    // 생성자: ErrorCode를 전달받아 설정
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
