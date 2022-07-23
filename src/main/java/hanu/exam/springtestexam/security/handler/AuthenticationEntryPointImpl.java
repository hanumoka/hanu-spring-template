package hanu.exam.springtestexam.security.handler;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.common.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 실패
 * - 입력 파라미터 포멧 및 형식등이 잘못된 경우
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        /**
         * 이곳에서 401 응답을 만들면 된다.
         */
        log.debug("AuthenticationEntryPointImpl commence");
        ApiResponse.error(response, HttpStatus.UNAUTHORIZED, ApiResponseCode.UNAUTHORIZED_RESPONSE);
    }
}

