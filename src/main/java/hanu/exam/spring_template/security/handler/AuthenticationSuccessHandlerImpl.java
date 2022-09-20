package hanu.exam.spring_template.security.handler;

import hanu.exam.spring_template.common.response.ApiResponse;
import hanu.exam.spring_template.security.token.CustomAuthResultToken;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인이 성공되면 토큰틀 생성해서 응답한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.warn("AuthenticationSuccessHandlerImpl onAuthenticationSuccess");
        // 전달받은 인증정보 SecurityContextHolder 에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomAuthResultToken customAuthResultToken = (CustomAuthResultToken) authentication;

        log.info("userId:" + customAuthResultToken.getUserId());
        log.info("userName:" + customAuthResultToken.getUsername());

        //생성된 리프래시토큰은 쿠키에 저장한다.
        String accessToken = jwtProvider.setRefreshTokenInCookie(customAuthResultToken.getUserId(),
                customAuthResultToken.getUsername(),
                response);

        //생성된 액세스토큰은 리스폰스로 응답한다.
        ApiResponse.accessToken(response, accessToken);
    }

}