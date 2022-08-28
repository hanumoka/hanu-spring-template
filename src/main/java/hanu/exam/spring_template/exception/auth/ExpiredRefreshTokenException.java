package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredRefreshTokenException extends CustomAuthException {

    public ExpiredRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
