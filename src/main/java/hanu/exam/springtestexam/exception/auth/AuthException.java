package hanu.exam.springtestexam.exception.auth;

import hanu.exam.springtestexam.common.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


@Getter
public abstract class AuthException extends AuthenticationException {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AuthException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public AuthException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

}
