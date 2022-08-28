package hanu.exam.spring_template.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 서비스의 정상적인 응답을 관리하는 클래스
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponse {

    private int code = ApiResponseCode.SUCCESS.getCode();
    private String msg = ApiResponseCode.SUCCESS.getMessage();

    //security에서 토큰 발행 응답용
    public static void accessToken(ServletResponse response, String accessToken) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);

        httpServletResponse.getWriter()
                .write(Objects.requireNonNull(
                        objectMapper.writeValueAsString(new DataApiResponse<Map<String, String>>(tokenMap)
                        )));
    }

    @Getter
    @AllArgsConstructor
    public static class ReplaceString {
        private final String key;
        private final String value;
    }


    //    public static ApiResponse error(ApiResponseCode apiResponseCode) {
//        return new ApiResponse(apiResponseCode.getCode(), apiResponseCode.getMessage());
//    }

    //    public static ApiResponse error(ApiResponseCode apiResponseCode, String Message) {
//        return new ApiResponse(apiResponseCode.getCode(), Message);
//    }
//
//    public static ApiResponse error(ApiResponseCode apiResponseCode, List<ReplaceString> replaceStringList) {
//        String message = apiResponseCode.getMessage();
//        for (ReplaceString replaceString : replaceStringList) {
//            message = message.replace(replaceString.getKey(), replaceString.getValue());
//        }
//        return new ApiResponse(apiResponseCode.getCode(), message);
//    }

//    public static void error(ServletResponse response, HttpStatus httpStatus) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setStatus(httpStatus.value());
//        httpServletResponse.getWriter().write(Objects.requireNonNull(objectMapper.writeValueAsString(httpStatus.getReasonPhrase())));
//    }
//
//    public static void error(ServletResponse response, HttpStatus httpStatus, ApiResponseCode apiResponseCode) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setStatus(httpStatus.value());
//        httpServletResponse.getWriter().write(Objects.requireNonNull(objectMapper.writeValueAsString(ApiResponse.error(apiResponseCode))));
//    }

//    public static void error(ServletResponse response, ApiResponseType apiResponseType) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setStatus(apiResponseType.getCode());
//        httpServletResponse.getWriter().write(Objects.requireNonNull(objectMapper.writeValueAsString(ApiResponse.error(apiResponseType))));
//    }

}