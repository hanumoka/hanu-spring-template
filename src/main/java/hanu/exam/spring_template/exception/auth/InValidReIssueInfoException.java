package hanu.exam.spring_template.exception.auth;

import hanu.exam.spring_template.common.response.ErrorCode;

public class InValidReIssueInfoException extends CustomAuthException {

    public InValidReIssueInfoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
