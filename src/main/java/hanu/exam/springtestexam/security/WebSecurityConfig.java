package hanu.exam.springtestexam.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.springtestexam.security.filter.CustomAuthorizationFilter;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

import static hanu.exam.springtestexam.security.AuthCustomDsl.customDsl;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${jwt.authorization-header}")
    private String AUTHORIZATION_HEADER;
    @Value("${jwt.header-name}")
    private String HEADER_NAME;


    // JWT ?????? ?????????
    private final JwtProvider jwtProvider;
    // ?????? ?????? ?????? ??????????????? ???????????? ???????????? ?????????
    private final AuthenticationEntryPoint authenticationEntryPoint;
    // ?????? ?????? ?????????
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    // ?????? ?????? ?????????
    private final AuthenticationFailureHandler authenticationFailureHandler;
    // ?????? ?????? ?????????
    private final AccessDeniedHandler accessDeniedHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security ignore ???????????????
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

        // GET ??????????????????
        final String[] GET_WHITELIST = new String[]{
        };

        // POST ??????????????????
        final String[] POST_WHITELIST = new String[]{
                "/signup",
                "/login"
        };

        //jwt ???????????? ????????? ?????? ?????? ??????
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        ;

        // ?????? ????????? security ??????
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, GET_WHITELIST).permitAll()
                .antMatchers(HttpMethod.POST, POST_WHITELIST).permitAll()

        ;


        // ???????????? ?????????
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // ???????????? ??????(401??????)
                .accessDeniedHandler(accessDeniedHandler) // ???????????? ??????(403??????)

        ;

        // ??????????????? ?????? URL??? ?????? ???????????? ??????
        http.authorizeRequests()
                .anyRequest().authenticated();

        //security??? /login ?????? ?????? ??????
        http.apply(customDsl(authenticationSuccessHandler, authenticationFailureHandler, objectMapper));

        //security??? jwt ?????? ???????????? ??????
        http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

//        configuration.addAllowedOrigin("*");  // ????????? URL,
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");  // ????????? Header
        configuration.addAllowedMethod("*");  // ????????? Http Method
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
