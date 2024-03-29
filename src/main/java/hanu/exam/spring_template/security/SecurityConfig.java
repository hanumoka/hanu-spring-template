package hanu.exam.spring_template.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.spring_template.config.SecurityPermitAllConfig;
import hanu.exam.spring_template.security.filter.JwtTokenFilter;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final SecurityPermitAllConfig securityPermitAllConfig;
    // JWT 제공 클래스
    private final JwtProvider jwtProvider;
    // 인증 실패 또는 인증헤더가 전달받지 못했을때 핸들러
    private final AuthenticationEntryPoint authenticationEntryPoint;
    // 인증 성공 핸들러
    private final AuthenticationSuccessHandler loginSuccessHandler;
    // 인증 실패 핸들러
    private final AuthenticationFailureHandler loginFailureHandler;
    // 인가 실패 핸들러
    private final AccessDeniedHandler accessDeniedHandler;
    private final ObjectMapper objectMapper;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security ignore 엔드포인트
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/swagger-ui/**"
                        ,"/v3/api-docs/**"
                );
    }

    @Bean
    public JwtTokenFilter customJwtTokenFilter() {
        return new JwtTokenFilter(jwtProvider);
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
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .antMatchers(HttpMethod.GET,
                        securityPermitAllConfig.getGetList().toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST,
                        securityPermitAllConfig.getPostList().toArray(new String[0])).permitAll()
                .anyRequest().permitAll()
        ;


        // 인가실패 헨들러
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인증실패 처리(401처리)
                .accessDeniedHandler(accessDeniedHandler) // 인가실패 처리(403처리)
        ;

        //security에 /login 인증 필터 연동
        http.apply(customDsl(loginSuccessHandler, loginFailureHandler, objectMapper, jwtProvider));

        //security에 jwt 토큰 인증필터 적용
        http.addFilterBefore(customJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

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
