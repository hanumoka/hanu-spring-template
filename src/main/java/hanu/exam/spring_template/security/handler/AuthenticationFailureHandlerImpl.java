package hanu.exam.spring_template.security.handler;

import hanu.exam.spring_template.common.ErrorCode;
import hanu.exam.spring_template.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 실패 응답처리
 * - 잘못된 username, password가 입력된 경우
 */
@Slf4j
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //TODO: 아이디, 패스워드를 다시 확인하라는 메시지 추가
        log.warn("AuthenticationFailureHandlerImpl onAuthenticationFailure");
//        ApiResponse.error(response, HttpStatus.UNAUTHORIZED, ApiResponseCode.UNAUTHORIZED_RESPONSE);
        ErrorResponse.error(response, HttpStatus.UNAUTHORIZED, ErrorCode.LOGIN_INPUT_INVALID);
    }

}
