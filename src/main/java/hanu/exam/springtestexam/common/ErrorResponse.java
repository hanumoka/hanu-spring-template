package hanu.exam.springtestexam.common;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    private String timestamp;
    private String path;
    private String controllerName;
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