package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.response.ErrorCode;

public class InvalidRefreshTokenException extends CustomAuthException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
