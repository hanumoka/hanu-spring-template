package hanu.exam.springtestexam.exception.auth;

import hanu.exam.springtestexam.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredAccessTokenException extends CustomAuthException {

    public ExpiredAccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
