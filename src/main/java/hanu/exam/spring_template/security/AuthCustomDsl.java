package hanu.exam.spring_template.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.spring_template.security.filter.LoginFilter;
import hanu.exam.spring_template.security.filter.ReissueTokenFilter;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthCustomDsl extends AbstractHttpConfigurer<AuthCustomDsl, HttpSecurity> {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtProvider jwtProvider;

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        LoginFilter loginFilter = new LoginFilter(authenticationManager, objectMapper);
        // 필터 URL 설정
        loginFilter.setFilterProcessesUrl("/api/v1/auth/login");
        // 인증 성공 핸들러
        loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 인증 실패 핸들러
        loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
        loginFilter.afterPropertiesSet();

        http.addFilter(loginFilter);

        ReissueTokenFilter reissueTokenFilter = new ReissueTokenFilter(authenticationManager, jwtProvider);
        // 필터 URL 설정
        reissueTokenFilter.setFilterProcessesUrl("/api/v1/auth/reissue");
        // 인증 성공 핸들러
        reissueTokenFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 인증 실패 핸들러
        reissueTokenFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
        reissueTokenFilter.afterPropertiesSet();

        http.addFilter(reissueTokenFilter);
    }


    public AuthCustomDsl(AuthenticationSuccessHandler authenticationSuccessHandler
            , AuthenticationFailureHandler authenticationFailureHandler
            , ObjectMapper objectMapper
            , JwtProvider jwtProvider
    ) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.objectMapper = objectMapper;
        this.jwtProvider = jwtProvider;
    }

    public static AuthCustomDsl customDsl(AuthenticationSuccessHandler authenticationSuccessHandler
            , AuthenticationFailureHandler authenticationFailureHandler
            , ObjectMapper objectMapper
            , JwtProvider jwtProvider
    ) {
        return new AuthCustomDsl(authenticationSuccessHandler, authenticationFailureHandler, objectMapper, jwtProvider);
    }
}