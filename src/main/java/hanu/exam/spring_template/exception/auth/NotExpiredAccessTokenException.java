package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "엑세스토큰이 만료되지 않음.")
public class NotExpiredAccessTokenException extends CustomAuthException {
    public NotExpiredAccessTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
