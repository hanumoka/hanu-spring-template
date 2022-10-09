package hanu.exam.spring_template.common.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "서버 응답 DTO")
@Getter
@NoArgsConstructor
@Builder
public class ComApiResponse<T> {

    @Schema(description = "서버응답코드")
    private int code = ResponseCode.SUCCESS.getCode();
    @Schema(description = "서버응답메세지")
    private String msg = ResponseCode.SUCCESS.getMessage();
    @Schema(description = "서버응답 result(데이터)")
    private T result;

    public ComApiResponse(int code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ComApiResponse(T result) {
        this.result = result;
    }

    public static void accessToken(ServletResponse response, String accessToken) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(ResponseCode.SUCCESS.getCode());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);

        httpServletResponse.getWriter()
                .write(Objects.requireNonNull(objectMapper.writeValueAsString(ComApiResponse.builder()
                        .result(tokenMap)
                        .build())));
    }
}
