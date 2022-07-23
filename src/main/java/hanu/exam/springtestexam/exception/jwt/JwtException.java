package hanu.exam.springtestexam.exception.jwt;

import hanu.exam.springtestexam.common.ErrorCode;
import hanu.exam.springtestexam.exception.BusinessException;

public abstract class JwtException extends BusinessException {
    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }

//    public JwtException(String message, ErrorCode errorCode) {
//        super(message, errorCode);
//    }
//
//    public JwtException(Throwable cause, ErrorCode errorCode) {
//        super(cause, errorCode);
//    }
//
//    public JwtException(String message, Throwable cause, ErrorCode errorCode) {
//        super(message, cause, errorCode);
//    }
}
