package hanu.exam.spring_template.common.response;

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
public class ComErrorResponse {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    private String timestamp;
    private String path;
    private int status;
    private String message;
    private String code;
    private List<FieldError> errors = new ArrayList<>();

    @JsonIgnore
    private String controllerName;

    @JsonIgnore
    private String methodName;

    @JsonIgnore
    private Throwable throwable;

    public static ComErrorResponse of(ErrorCode errorCode
            , String controllerName, String methodName, String path
    ) {
        return ComErrorResponse.builder()
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

    public static ComErrorResponse of(ErrorCode errorCode, String path) {
        return ComErrorResponse.builder()
                .timestamp(sdf.format(System.currentTimeMillis()))
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .path(path)
                .errors(new ArrayList<>())
                .build();
    }

    public static ComErrorResponse of(ErrorCode errorCode, String path, Throwable throwable) {
        return ComErrorResponse.builder()
                .timestamp(sdf.format(System.currentTimeMillis()))
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .path(path)
                .errors(new ArrayList<>())
                .throwable(throwable)
                .build();
    }

    public static ComErrorResponse of(ErrorCode errorCode, Throwable throwable) {
        return ComErrorResponse.builder()
                .timestamp(sdf.format(System.currentTimeMillis()))
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(new ArrayList<>())
                .throwable(throwable)
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
                        objectMapper.writeValueAsString(ComErrorResponse.of(errorCode, path)))
                );
    }

    public static void error(ServletResponse response, HttpStatus httpStatus, ErrorCode errorCode)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse
                .getWriter().
                write(Objects.requireNonNull(
                        objectMapper.writeValueAsString(ComErrorResponse.of(errorCode, "")))
                );
    }

    public static void error(ServletResponse response,
                             HttpStatus httpStatus,
                             ErrorCode errorCode,
                             Throwable throwable
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.getWriter().write(
                Objects.requireNonNull(objectMapper.writeValueAsString(ComErrorResponse.of(errorCode, throwable))));
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}