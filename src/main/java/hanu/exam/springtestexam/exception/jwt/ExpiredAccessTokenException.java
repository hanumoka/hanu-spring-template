package hanu.exam.springtestexam.exception.jwt;

import hanu.exam.springtestexam.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredAccessTokenException extends JwtException {

    public ExpiredAccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
