package hanu.exam.springtestexam.security;

import hanu.exam.springtestexam.security.filter.CustomAuthorizationFilter;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
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

import static hanu.exam.springtestexam.security.AuthCustomDsl.customDsl;


@Configuration
//@EnableWebSecurity , WebSecurityConfigurerAdapter가 derecated 되어 더이상 필요 없을듯 하다.
@RequiredArgsConstructor
public class WebSecurityConfig {

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

//    @Bean
//    public CustomAuthorizationFilter jwtFilter() {
//        return new CustomAuthorizationFilter(jwtProvider);
//    }

    @Bean
//    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        final String[] GET_WHITELIST = new String[]{
//                "/login" // 로그인
        };

        final String[] POST_WHITELIST = new String[]{
//                "/user" // 회원가입
                "/login"
        };


        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 off
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인가실패 응답
                .accessDeniedHandler(accessDeniedHandler) // 인가 실패
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, GET_WHITELIST).permitAll() // 해당 GET URL은 모두 허용
                .antMatchers(HttpMethod.POST, POST_WHITELIST).permitAll() // 해당 POST URL은 모두 허용
//                .antMatchers("**").hasAnyRole("USER") // 권한 적용
                .anyRequest().authenticated() // 나머지 요청에 대해서는 인증을 요구
                .and()
                .formLogin().disable() // 로그인 페이지 사용 안함
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 인증필터
//                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)  // jwt 인증필터
                .apply(customDsl(authenticationSuccessHandler, authenticationFailureHandler))
        ;

        return http.build();
    }

    /**
     * 사용자 요청 정보로 UserPasswordAuthenticationToken 발급하는 필터
     */
//    @Bean
//    public CustomAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
//        // 필터 URL 설정
//        customAuthenticationFilter.setFilterProcessesUrl("/login");
//        // 인증 성공 핸들러
//        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
//        // 인증 실패 핸들러
//        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//        // BeanFactory에 의해 모든 property가 설정되고 난 뒤 실행
//        customAuthenticationFilter.afterPropertiesSet();
//        return customAuthenticationFilter;
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//            throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }


}
