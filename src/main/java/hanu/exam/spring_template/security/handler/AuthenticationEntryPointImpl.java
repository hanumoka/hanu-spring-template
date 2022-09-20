package hanu.exam.spring_template.security.handler;


import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.common.response.ErrorResponse;
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
 * CustomJwtTokenFilter에서 setAuthentication 을 하지못하면 해당 클래스의 commence가 호출된다.
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
        ErrorResponse.error(response, HttpStatus.UNAUTHORIZED, ErrorCode.JWT_EXPIRED_ACCESS_TOKEN, authException);
    }
}

