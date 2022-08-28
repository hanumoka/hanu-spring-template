package hanu.exam.spring_template.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.spring_template.common.HttpUtil;
import hanu.exam.spring_template.security.token.CustomAuthenticationToken;
import hanu.exam.spring_template.security.jwt.JwtTokenDto;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER;
    private final String HEADER_NAME;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    /**
     * 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
     * 주의!
     * - 이 필터는 security에서 검증할 데이터를 수집하는 필터이다.(header의 accesstoken정보)
     * - 실질적인 검증 기능을 본 필터에서 하면 안된다. => AuthenticationProviderImpl에게 넘기자.
     * - DB같은 곳에 조회 같은것을 하지 말자. 이녀석은 문지기
     * 주된 처리
     * -
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("CustomAuthorizationFilter doFilterInternal");

        // Request Header에서 토큰 추출
        String jwt = resolveAccessToken(request);
        log.info("jwt:" + jwt);

        // AccessToken 유효성 검사
        if (StringUtils.hasText(jwt)) {
            JwtTokenDto jwtTokenDto;

            try{
                //토큰의 유효성 검사
                jwtTokenDto = jwtProvider.validateAccessToken(jwt);

                // 토큰으로 인증 정보를 추출
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Authentication authentication = new CustomAuthenticationToken(jwtTokenDto.getUserId(), jwtTokenDto.getUsername(), authorities);
                // SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch(TokenExpiredException tee){
                logger.info("accessToken 만료됨...");

                //TODO: 리프레시 토큰 존재하는지 확인
                String refreshToken = resolveRefreshToken(request);
                if(StringUtils.hasText(refreshToken)){
                    log.debug("refreshToken 존재함:" + refreshToken);
                }else{
                    log.warn("refreshToken 없음");
                }// else

            } catch(Exception e){
                logger.info("accessToken 벨리데이션 예외발생");
                throw e;
            } // catch
        } //if

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 accessToken 토큰 추출
     */
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_NAME)) {
            return bearerToken.substring(7);
        } //if
        return null;
    }

    /**
     * request 쿠카에서 refresh-toekn 추철
     */
    private String resolveRefreshToken(HttpServletRequest request){
        log.info("resolveRefreshToken start--->");
        return HttpUtil.getRefreshToken(request);
    }

}