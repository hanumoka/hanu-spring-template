package hanu.exam.spring_template.security.handler;

import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.common.response.ComErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 필터에서
 * attemptAuthentication 진입직후 이증 실패시 동작
 */
@Slf4j
@Component("loginFailureHandler")
public class LoginFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //TODO: 아이디, 패스워드를 다시 확인하라는 메시지 추가
        log.warn("LoginFailureHandlerImpl onAuthenticationFailure");
//        ApiResponse.error(response, HttpStatus.UNAUTHORIZED, ApiResponseCode.UNAUTHORIZED_RESPONSE);
        ComErrorResponse.error(response, HttpStatus.UNAUTHORIZED, ErrorCode.LOGIN_INPUT_INVALID);
    }

}
