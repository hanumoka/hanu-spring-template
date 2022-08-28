package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


@Getter
public abstract class CustomAuthException extends AuthenticationException {

    private final ErrorCode errorCode;

    public CustomAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomAuthException(String msg, Throwable cause, ErrorCode errorCode) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public CustomAuthException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

}
