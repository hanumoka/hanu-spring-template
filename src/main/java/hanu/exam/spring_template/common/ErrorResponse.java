package hanu.exam.spring_template.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO: 해당응답이 발생시 해당 내용을 취합해서 aws의 클라우드와치에 전달하거나 별도의 에러응답 로그를 생성해도 될것 같다.

/**
 * 정상적인 응답이 아닌 응답을 처리하는 클래스
 */
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

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .timestamp(sdf.format(System.currentTimeMillis()))
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .path(path)
                .errors(new ArrayList<>())
                .build();
    }

    public static void error(ServletResponse response, HttpStatus httpStatus, ErrorCode errorCode, String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse
                .getWriter().
                write(Objects.requireNonNull(
                                objectMapper.writeValueAsString(ErrorResponse.of(errorCode, path)))
                );
    }

    public static void error(ServletResponse response, HttpStatus httpStatus, ErrorCode errorCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse
                .getWriter().
                write(Objects.requireNonNull(
                        objectMapper.writeValueAsString(ErrorResponse.of(errorCode, "")))
                );
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}