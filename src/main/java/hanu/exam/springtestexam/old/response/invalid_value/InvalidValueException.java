package hanu.exam.springtestexam.old.response.invalid_value;

import hanu.exam.springtestexam.common.ErrorCode;
import hanu.exam.springtestexam.exception.BusinessException;

public abstract class InvalidValueException extends BusinessException {
    public InvalidValueException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidValueException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public InvalidValueException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }
}
