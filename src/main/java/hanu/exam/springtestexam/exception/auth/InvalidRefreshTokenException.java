package hanu.exam.springtestexam.exception.auth;

import hanu.exam.springtestexam.common.ErrorCode;

public class InvalidRefreshTokenException extends CustomAuthException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
