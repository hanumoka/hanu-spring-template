package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.response.ErrorCode;

public class InvalidLoginInfoException extends CustomAuthException {

    public InvalidLoginInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
