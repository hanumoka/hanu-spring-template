package hanu.exam.springtestexam.security.handler;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.domain.account.entity.Account;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 전달받은 인증정보 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT Token 발급 - accessToken
        String accessToken = jwtProvider.createAccessToken(authentication.getName()
                , null
                , request.getRequestURI());


        // JWT Token 발급 - refreshToken
        String refreshToken = jwtProvider.createAccessToken(authentication.getName()
                , null
                , request.getRequestURI());
        // Response
        System.out.println("accessToken username:" + jwtProvider.validateToken(accessToken));
        System.out.println("refreshToken username:" + jwtProvider.validateToken(refreshToken));
        ApiResponse.token(response, accessToken, refreshToken);
    }

}