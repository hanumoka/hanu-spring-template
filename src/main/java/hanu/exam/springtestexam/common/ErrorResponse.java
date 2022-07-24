package hanu.exam.springtestexam.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//TODO: 해당응답이 발생시 해당 내용을 취합해서 aws의 클라우드와치에 전달하거나 별도의 에러응답 로그를 생성해도 될것 같다.
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    private String timestamp;
    private String path;
    @JsonIgnore
    private String controllerName;
    @JsonIgnore
    private String methodName;
    private int status;
    private String message;
    private String code;
    private List<FieldError> errors = new ArrayList<>();


    public static ErrorResponse of(ErrorCode errorCode
            , String controllerName, String methodName, String path
    ) {
        return ErrorResponse.builder()
                .timestamp(sdf.format(System.currentTimeMillis()))
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .controllerName(controllerName)
                .methodName(methodName)
                .path(path)
                .errors(new ArrayList<>())
                .build();
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}