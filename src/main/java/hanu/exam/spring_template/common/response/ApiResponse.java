package hanu.exam.spring_template.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code = ResponseCode.SUCCESS.getCode();
    private String msg = ResponseCode.SUCCESS.getMessage();
    private T result;
}
