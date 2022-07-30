package hanu.exam.springtestexam.exception.auth;

import hanu.exam.springtestexam.common.ErrorCode;

public class InValidReIssueInfoException extends AuthException{

    public InValidReIssueInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
