package hanu.exam.springtestexam.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.springtestexam.security.filter.CustomAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthCustomDsl extends AbstractHttpConfigurer<AuthCustomDsl, HttpSecurity> {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager, objectMapper);
        // 필터 URL 설정
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        // 인증 성공 핸들러
        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        // 인증 실패 핸들러
        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
        customAuthenticationFilter.afterPropertiesSet();

        http.addFilter(customAuthenticationFilter);
    }


    public AuthCustomDsl(AuthenticationSuccessHandler authenticationSuccessHandler
            , AuthenticationFailureHandler authenticationFailureHandler
            , ObjectMapper objectMapper
    ) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.objectMapper = objectMapper;
    }

    public static AuthCustomDsl customDsl(AuthenticationSuccessHandler authenticationSuccessHandler
            , AuthenticationFailureHandler authenticationFailureHandler
            , ObjectMapper objectMapper
    ) {
        return new AuthCustomDsl(authenticationSuccessHandler, authenticationFailureHandler, objectMapper);
    }
}