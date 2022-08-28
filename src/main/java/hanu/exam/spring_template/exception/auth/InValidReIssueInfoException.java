package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.ErrorCode;

public class InValidReIssueInfoException extends CustomAuthException {

    public InValidReIssueInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
