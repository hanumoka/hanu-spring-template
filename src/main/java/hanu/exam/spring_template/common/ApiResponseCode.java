package hanu.exam.spring_template.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseCode {

    SUCCESS(200, "Success"),

    //TODO: 아래 내용 수정해야됨. http 응답코드는 구현체를 사용하자. 여기서는 백엔드의 커스텀 상세 메세지용도로 사용하자.
    UNAUTHORIZED_RESPONSE(401, "Unauthorized"),
    FORBIDDEN_RESPONSE(403, "Forbidden"),
    NOT_FOUND_RESPONSE(404, "Not Found"),
    METHOD_NOT_ALLOWED_RESPONSE(405, "Method Not Allowed"),

    NOT_VALID_RESPONSE(409, "Not Valid"),
    NOT_FOUND_DATA_RESPONSE(409, "Not Found Data({ENTITY})"),
    ALREADY_DATA_RESPONSE(409, "Already Data({ENTITY})"),
    PARSE_ERROR_RESPONSE(409, "Parsing Error"),
    NOT_PRIMARY_ERROR_RESPONSE(409, "Not Primary Key Error"),
    SQL_ERROR_RESPONSE(409, "SQL Error"),
    ILLEGAL_ACCESS_RESPONSE(409, "Illegal Access"),

    TO_MANY_REQUESTS_RESPONSE(429, "Too Many Requests");


    private final int code;
    private final String message;

}