package hanu.exam.springtestexam.common;

import lombok.Getter;

@Getter
public enum ErrorCode {


    // 500
    INTERNAL_SERVER_ERROR(500, "500", "INTERNAL_SERVER_ERROR"),

    //jwt
    JWT_NOT_EXPIRED_ACCESS_TOKEN(400, "J001", "Not Expired AccessToken."),
    JWT_EXPIRED_ACCESS_TOKEN(400, "J002", "Expired AccessToken."),

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),

            ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}