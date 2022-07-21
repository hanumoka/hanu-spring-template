package hanu.exam.springtestexam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExpiredAccessTokenException extends RuntimeException {

    public NotExpiredAccessTokenException(String message) {
        super(message);
    }
}
