package hanu.exam.springtestexam.security;

import hanu.exam.springtestexam.security.filter.CustomAuthorizationFilter;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static hanu.exam.springtestexam.security.AuthCustomDsl.customDsl;


@Configuration
//@EnableWebSecurity , WebSecurityConfigurerAdapter가 derecated 되어 더이상 필요 없을듯 하다.
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${jwt.authorization-header}")
    private String AUTHORIZATION_HEADER;
    @Value("${jwt.header-name}")
    private String HEADER_NAME;


    // JWT 제공 클래스
    private final JwtProvider jwtProvider;
    // 인증 실패 또는 인증헤더가 전달받지 못했을때 핸들러
    private final AuthenticationEntryPoint authenticationEntryPoint;
    // 인증 성공 핸들러
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    // 인증 실패 핸들러
    private final AuthenticationFailureHandler authenticationFailureHandler;
    // 인가 실패 핸들러
    private final AccessDeniedHandler accessDeniedHandler;

//    private final AuthenticationManager authenticationManager;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security ignore 엔드포인트
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter(AUTHORIZATION_HEADER, HEADER_NAME, jwtProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // GET 화이트리스트
        final String[] GET_WHITELIST = new String[]{
        };

        // POST 화이트리스트
        final String[] POST_WHITELIST = new String[]{
                "/signup",
                "/login"
        };

        // disable cors
        http.cors().disable();

        // disable csrf
        http.csrf().disable();

        // disable form login, (필요한지 잘 모르겠다.)
        http.formLogin().disable();

        // Set session management to stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 특정 도메인 security 허용
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, POST_WHITELIST).permitAll();


        // 기본적으로 모든 URL에 대한 권한검증 처리
        http.authorizeRequests()
                .anyRequest().authenticated();
         // 인가실패 헨들러
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인증실패 처리(401처리)
                .accessDeniedHandler(accessDeniedHandler) // 인가실패 처리(403처리)
        ;

        //security에 /login처리 연동
        http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)  // jwt 인증필터
                .apply(customDsl(authenticationSuccessHandler, authenticationFailureHandler))
        ;

        return http.build();
    }


}
