package hanu.exam.spring_template.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private int code = ResponseCode.SUCCESS.getCode();
    private String msg = ResponseCode.SUCCESS.getMessage();
    private T result;

    public static void accessToken(ServletResponse response, String accessToken) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(ResponseCode.SUCCESS.getCode());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);

        httpServletResponse.getWriter()
                .write(Objects.requireNonNull(objectMapper.writeValueAsString(ApiResponse.builder()
                        .result(tokenMap)
                        .build())));
    }
}
