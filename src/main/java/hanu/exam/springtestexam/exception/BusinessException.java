package hanu.exam.springtestexam.exception;

import hanu.exam.springtestexam.common.ErrorCode;
import lombok.Getter;

/**
 * 비지니스 최상위 예외
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;


    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
