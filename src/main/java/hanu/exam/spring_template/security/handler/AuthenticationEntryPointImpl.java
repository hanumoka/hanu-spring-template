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

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     * 1.로그인 진입 실패시
     * 2.토큰 재발행 진입 실패시
     *
     * 아래 메소드 동작
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("AuthenticationEntryPointImpl commence");

        ErrorCode errorCode = (ErrorCode)request.getAttribute("exception");

        log.warn("errorCode: {}", errorCode);

        // TODO: 아래에서 로그인, 토큰 재발행 요청시 오류를 구분하여 처리하자.

//        if(exception == null) {
//            setResponse(response, ExceptionCode.UNKNOWN_ERROR);
//        }
//        //잘못된 타입의 토큰인 경우
//        else if(exception.equals(ExceptionCode.WRONG_TYPE_TOKEN.getCode())) {
//            setResponse(response, ExceptionCode.WRONG_TYPE_TOKEN);
//        }
//        //토큰 만료된 경우
//        else if(exception.equals(ExceptionCode.EXPIRED_TOKEN.getCode())) {
//            setResponse(response, ExceptionCode.EXPIRED_TOKEN);
//        }
//        //지원되지 않는 토큰인 경우
//        else if(exception.equals(ExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
//            setResponse(response, ExceptionCode.UNSUPPORTED_TOKEN);
//        }
//        else {
//            setResponse(response, ExceptionCode.ACCESS_DENIED);
//        }

        ErrorResponse.error(response, HttpStatus.UNAUTHORIZED, ErrorCode.JWT_EXPIRED_ACCESS_TOKEN, authException);
    }
}

