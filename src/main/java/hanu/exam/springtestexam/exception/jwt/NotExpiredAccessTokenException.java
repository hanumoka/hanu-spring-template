package hanu.exam.springtestexam.exception.jwt;

import hanu.exam.springtestexam.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "엑세스토큰이 만료되지 않음.")
public class NotExpiredAccessTokenException extends JwtException {

    public NotExpiredAccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
