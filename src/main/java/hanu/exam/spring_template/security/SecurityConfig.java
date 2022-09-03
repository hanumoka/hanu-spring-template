package hanu.exam.spring_template.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.spring_template.config.SecurityPermitAllConfig;
import hanu.exam.spring_template.security.filter.CustomJwtTokenFilter;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static hanu.exam.spring_template.security.AuthCustomDsl.customDsl;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${hanu.jwt.authorization-header}")
    private String AUTHORIZATION_HEADER;
    @Value("${hanu.jwt.header-name}")
    private String HEADER_NAME;

    private final SecurityPermitAllConfig securityPermitAllConfig;


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
    private final ObjectMapper objectMapper;

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
    public CustomJwtTokenFilter customJwtTokenFilter() {
        return new CustomJwtTokenFilter(AUTHORIZATION_HEADER, HEADER_NAME, jwtProvider, objectMapper);
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //jwt 토큰방식 적용을 위한 기초 설정
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;

        // 특정 도메인 security 허용
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,
                        securityPermitAllConfig.getGetList().toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST,
                        securityPermitAllConfig.getPostList().toArray(new String[0])).permitAll()
        ;


        // 인가실패 헨들러
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인증실패 처리(401처리)
                .accessDeniedHandler(accessDeniedHandler) // 인가실패 처리(403처리)
        ;

        // 기본적으로 모든 URL에 대한 권한검증 처리
        http.authorizeRequests()
                .anyRequest().authenticated();

        //security에 /login 인증 필터 연동
        http.apply(customDsl(authenticationSuccessHandler, authenticationFailureHandler, objectMapper));

        //security에 jwt 토큰 인증필터 적용
        http.addFilterBefore(customJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(customReissueTokensFilter(), CustomJwtTokenFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("*");  // 허용할 URL,
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");  // 허용할 Header
        configuration.addAllowedMethod("*");  // 허용할 Http Method
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
