package hanu.exam.spring_template.security.handler;

import hanu.exam.spring_template.common.ApiResponse;
import hanu.exam.spring_template.common.HttpUtil;
import hanu.exam.spring_template.security.token.CustomAuthenticationToken;
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

    @Value("${hanu.service.name}")
    private String serviceName;

    private final JwtProvider jwtProvider;

    @Value("${hanu.jwt.refresh-token.expire-length}")
    private long REFRESH_VALIDITY_IN_MILLISECONDS;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 전달받은 인증정보 SecurityContextHolder 에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;

        log.info("userId:" + customAuthenticationToken.getUserId());
        log.info("userName:" + customAuthenticationToken.getUsername());

        // JWT Token 발급 - accessToken
        String accessToken = jwtProvider.createAccessToken(
                customAuthenticationToken.getUserId()
                , customAuthenticationToken.getUsername()
                , serviceName);

        // JWT Token 발급 - refreshToken
        String refreshToken = jwtProvider.createAccessToken(
                customAuthenticationToken.getUserId()
                , customAuthenticationToken.getUsername()
                , serviceName);

        HttpUtil.setRefreshToken(response, refreshToken, REFRESH_VALIDITY_IN_MILLISECONDS / 1000);

        ApiResponse.accessToken(response, accessToken);
    }

}