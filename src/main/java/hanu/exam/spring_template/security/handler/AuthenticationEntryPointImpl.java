package hanu.exam.spring_template.security.handler;


import hanu.exam.spring_template.common.ErrorCode;
import hanu.exam.spring_template.common.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * request로 부터 유효한 jwt 토큰아 없는 경우 해당 commence가 호출된다.
 * AuthenticationEntryPointImpl 이름을 가지고 있지만 실제로 검증이 필요한 요청에 유효한 토큰이 없는경우 해당 로직이 동작한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     *
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("AuthenticationEntryPointImpl commence");
        ErrorResponse.error(response, HttpStatus.UNAUTHORIZED, ErrorCode.JWT_EXPIRED_ACCESS_TOKEN);
    }
}

