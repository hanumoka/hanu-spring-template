package hanu.exam.springtestexam.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.springtestexam.security.jwt.JwtCookieProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtCookieProvider jwtCookieProvider;
//    private final IUserService IUserService;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtCookieProvider jwtCookieProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtCookieProvider = jwtCookieProvider;
//        this.IUserService = IUserService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //인증 시도시 호출
        log.info("login 시도 ...");
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username;
        String password;
        try {
            Map requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = requestMap.get("username").toString();
            password = requestMap.get("password").toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        log.info("Username is : {}", username);
        log.info("Password is : {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        //인증 성공시 호출, 주의 아래 User는 UserDetails의 User
        User user = (User) authentication.getPrincipal();

//        com.hanumoka.sebure.domain.User loginUser = userService.getUser(user.getUsername());
//        UserDto.SafeInfo loginUser = IUserService.getSafeInfo(user.getUsername());
//        log.info("login user: {}", loginUser);
//
//        String refresh_token = jwtCookieProvider.createRefreshToken(user, request.getRequestURI());
//        RefreshToken savedRefreshToken = jwtCookieProvider.saveRefreshToken(refresh_token);
//        String access_token = jwtCookieProvider.createAccessToken(user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), request.getRequestURI(), savedRefreshToken.getId());
//
//        response = jwtCookieProvider.createCookie(response, access_token); // access_token은 쿠키에 저장
//
//        Map<String, Object> message = new HashMap<>();
//        message.put("success", true);
//        message.put("username", loginUser.getUsername());
//        message.put("nickname", loginUser.getNickname());
//        message.put("sebureUri", loginUser.getSebureUri());
//        message.put("introduction", loginUser.getIntroduction());
//        message.put("profileImageUri", loginUser.getProfileImageUri());

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), "인증성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("unsuccessfulAuthentication ...");
        SecurityContextHolder.clearContext();

        log.error("Error logging in: {}", exception.getMessage());
        // 토큰이 유효하지 않은 경우
        response.setStatus(UNAUTHORIZED.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
