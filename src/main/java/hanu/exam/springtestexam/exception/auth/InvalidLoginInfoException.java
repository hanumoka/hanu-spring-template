package hanu.exam.springtestexam.exception.auth;

import hanu.exam.springtestexam.common.ErrorCode;

public class InvalidLoginInfoException extends CustomAuthException {

    public InvalidLoginInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
