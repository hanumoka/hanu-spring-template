package hanu.exam.springtestexam.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.springtestexam.security.jwt.JwtCookieProvider;
import hanu.exam.springtestexam.security.jwt.ValidatedUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtCookieProvider jwtCookieProvider;
//    private final ITokenService refreshITokenService;
//    private final IUserService IUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("CustomAuthorizationFilter doFilterInternal 시작 url: {}", request.getServletPath());

//        if (request.getServletPath().equals("/api/user/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String refreshTokenId = null;
//        String accessToken = null;
//        accessToken = jwtCookieProvider.resolveCookie(request);
//        log.info("accessToken : {}", accessToken);
//
//        if (accessToken == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        try {
//            ValidatedUserInfo validatedUserInfo = jwtCookieProvider.validateToken(accessToken);
//
//            String username = validatedUserInfo.getUsername();
//            String[] roles = validatedUserInfo.getRoles();
//
//            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
////            stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));  // TODO: 권한 임시주석
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            filterChain.doFilter(request, response);
//        } catch (TokenExpiredException tokenExpiredException) {
//            log.error("Error logging1 in: {}", tokenExpiredException.getMessage());
//            // 액세스토큰 만료시 엑세스 토큰에서 리프레시 토큰 id를 가져온다.
//            refreshTokenId = jwtCookieProvider.getRefreshTokenIdTokenFromAccessToken(accessToken);
//            log.info("refreshTokenId : {}", refreshTokenId);
//        }  catch (Exception exception) {
//            exception.printStackTrace();
//            log.error("Error logging3 in: {}", exception.getMessage());
//            response.setStatus(FORBIDDEN.value());
//            Map<String, String> error = new HashMap<>();
//            error.put("error_message", "로그인이 필요합니다.");
//            response.setContentType(APPLICATION_JSON_VALUE);
//            jwtCookieProvider.deleteCookie(response);
//            new ObjectMapper().writeValue(response.getOutputStream(), error);
//        }
//
//        if (StringUtils.isNotBlank(refreshTokenId)) {
//            // 리프레시토큰으로 액세토큰 재발급 시도
//
//            // 1, 리프레시토큰을 DB에서 조회
//            RefreshToken refreshToken = refreshITokenService.getRefreshToken(Long.parseLong(refreshTokenId));
//            // 2. 리프레시토큰 벨리데이트 검사
//            if (refreshToken != null) {
//                try {
//                    ValidatedUserInfo validatedUserInfo = jwtCookieProvider.validateToken(refreshToken.getToken());
//                    User user = IUserService.getUser(validatedUserInfo.getUsername());
//                    log.info("refresh 토큰 유효 username: {}", user.getUsername());
//
//
//                    // TODO: 권한임시주석
////                    String access_token = jwtCookieProvider.createAccessToken(user.getUsername()
////                            , user.getRoles().stream().map(Role::getName).toList()
////                            , request.getRequestURI()
////                            , refreshToken.getId());
//
//                    String access_token = jwtCookieProvider.createAccessToken(user.getUsername()
//                            , null
//                            , request.getRequestURI()
//                            , refreshToken.getId());
//
//                    response = jwtCookieProvider.createCookie(response, access_token); // access_token은 쿠키에 저장
//                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
////                    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));  // TODO: 권한 임시주석
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    log.info("액세스 토큰 재 갱신");
//                    filterChain.doFilter(request, response);
//
//                } catch (Exception e){
//                    log.error("리프레시 토큰 만료");
//                    e.printStackTrace();
//                    log.error("Error logging4 in: {}", e.getMessage());
//                    response.setStatus(FORBIDDEN.value());
//                    Map<String, String> error = new HashMap<>();
//                    error.put("error_message", "1.로그인이 만료 되었습니다.");
//                    response.setContentType(APPLICATION_JSON_VALUE);
//                    jwtCookieProvider.deleteCookie(response);
//                    new ObjectMapper().writeValue(response.getOutputStream(), error);
//                }
//            } else {
//                log.error("DB에 해당 리프레시토큰 없음 id: {}", refreshTokenId);
//                response.setStatus(UNAUTHORIZED.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", "2.로그인이 만료 되었습니다.");
//                response.setContentType(APPLICATION_JSON_VALUE);
//                jwtCookieProvider.deleteCookie(response);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }// else
//        } //if





        // 요청으보부터 JWT Token을 검사하는 필터
        // TODO: 토큰 검사할 white list이거 분리해야 할듯
//        if (request.getServletPath().equals("/api/login")
//                || request.getServletPath().startsWith("/api/publics")
//                || request.getServletPath().startsWith("/api/getImage")
//        ) {
//            log.info("토근검사 안함... request.getServletPath():" + request.getServletPath());
//            // 로그인 요청에서는 아무것도 안함
//            filterChain.doFilter(request, response);
//        } else {
//            log.info("토큰검사 함...");
//            String refreshTokenId = null;
//            String accessToken = null;
//            try {
//                accessToken = jwtCookieProvider.resolveCookie(request);
//                log.info("accessToken : {}", accessToken);
//                ValidatedUserInfo validatedUserInfo = jwtCookieProvider.validateToken(accessToken);
//
//                String username = validatedUserInfo.getUsername();
//                String[] roles = validatedUserInfo.getRoles();
//
//                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                filterChain.doFilter(request, response);
//            } catch (TokenExpiredException tokenExpiredException) {
//                log.error("Error logging1 in: {}", tokenExpiredException.getMessage());
//                // 액세스토큰 만료시 엑세스 토큰에서 리프레시 토큰 id를 가져온다.
//                refreshTokenId = jwtCookieProvider.getRefreshTokenIdTokenFromAccessToken(accessToken);
//                log.info("refreshTokenId : {}", refreshTokenId);
//            } catch (JWTVerificationException jwtVerificationException) {
//                log.error("Error logging2 in: {}", jwtVerificationException.getMessage());
//
//                if(request.getServletPath().startsWith("/api/public")){
//                    // TODO: 리팩토링 필요
//                    System.out.println("========== 진입함......");
//                    filterChain.doFilter(request, response);
//                }else{
//                    response.setStatus(UNAUTHORIZED.value());
//                    Map<String, String> error = new HashMap<>();
//                    error.put("error_message", jwtVerificationException.getMessage());
//                    response.setContentType(APPLICATION_JSON_VALUE);
//                    jwtCookieProvider.deleteCookie(response);
//                    new ObjectMapper().writeValue(response.getOutputStream(), error);
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                log.error("Error logging3 in: {}", exception.getMessage());
//                response.setStatus(UNAUTHORIZED.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", "로그인이 필요합니다.");
//                response.setContentType(APPLICATION_JSON_VALUE);
//                jwtCookieProvider.deleteCookie(response);
//                new ObjectMapper().writeValue(response.getOutputStream(), error);
//            }
//
//            if (StringUtils.isNotBlank(refreshTokenId)) {
//                // 리프레시토큰으로 액세토큰 재발급 시도
//
//                // 1, 리프레시토큰을 DB에서 조회
//                RefreshToken refreshToken = refreshITokenService.getRefreshToken(Long.parseLong(refreshTokenId));
//                // 2. 리프레시토큰 벨리데이트 검사
//                if (refreshToken != null) {
//                    try {
//                        ValidatedUserInfo validatedUserInfo = jwtCookieProvider.validateToken(refreshToken.getToken());
//                        User user = IUserService.getUser(validatedUserInfo.getUsername());
//                        log.info("refresh 토큰 유효 username: {}", user.getUsername());
//
//                        String access_token = jwtCookieProvider.createAccessToken(user.getUsername()
//                                , user.getRoles().stream().map(Role::getName).toList()
//                                , request.getRequestURI()
//                                , refreshToken.getId());
//
//                        response = jwtCookieProvider.createCookie(response, access_token); // access_token은 쿠키에 저장
//                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
//                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                        log.info("액세스 토큰 재 갱신");
//                        filterChain.doFilter(request, response);
//
//                    } catch (TokenExpiredException tokenExpiredException) {
//                        log.error("리프레시 토큰 만료");
//                        log.error("Error logging4 in: {}", tokenExpiredException.getMessage());
//                        response.setStatus(UNAUTHORIZED.value());
//                        Map<String, String> error = new HashMap<>();
//                        error.put("error_message", "로그인이 만료 되었습니다.");
//                        //TODO: 액세스 토큰 쿠키 제거 필요하다.
//                        response.setContentType(APPLICATION_JSON_VALUE);
//                        jwtCookieProvider.deleteCookie(response);
//                        new ObjectMapper().writeValue(response.getOutputStream(), error);
//                    } catch (JWTVerificationException jwtVerificationException) {
//                        log.error("리프레시 토큰 벨리데이션 오류");
//                        //TODO: 액세스 토큰 쿠키 제거 필요하다.
//                        log.error("Error logging5 in: {}", jwtVerificationException.getMessage());
//                        response.setStatus(UNAUTHORIZED.value());
//                        Map<String, String> error = new HashMap<>();
//                        error.put("error_message", "로그인이 만료 되었습니다.");
//                        response.setContentType(APPLICATION_JSON_VALUE);
//
//                        jwtCookieProvider.deleteCookie(response);
//                        new ObjectMapper().writeValue(response.getOutputStream(), error);
//                    }
//                } else {
//                    log.error("DB에 해당 리프레시토큰 없음 id: {}", refreshTokenId);
//                    response.setStatus(UNAUTHORIZED.value());
//                    Map<String, String> error = new HashMap<>();
//                    error.put("error_message", "로그인이 만료 되었습니다.");
//                    response.setContentType(APPLICATION_JSON_VALUE);
//                    new ObjectMapper().writeValue(response.getOutputStream(), error);
//                }// else
//            }//if
//        }//else
    }
}