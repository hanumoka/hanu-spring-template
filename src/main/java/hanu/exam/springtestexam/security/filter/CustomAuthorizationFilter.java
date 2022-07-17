package hanu.exam.springtestexam.security.filter;

import hanu.exam.springtestexam.security.CustomAuthenticationToken;
import hanu.exam.springtestexam.security.dto.JWTTokenDTO;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER;
    private final String BEARER_PREFIX;
    private final JwtProvider jwtProvider;

    /**
     * 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
     * 주의!
     * - 이 필터는 security에서 검증할 데이터를 수집하는 필터이다.(header의 accesstoken정보)
     * - 실질적인 검증 기능을 본 필터에서 하면 안된다.
     * - 데이터가 없다면 그냥 다음 필터체인으로 넘거야 한다.
     * - 따라서 본 필터에서 예외를 발생시키거나 절대 직접 응답을 하면 안된다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Request Header에서 토큰 추출
        String jwt = resolveToken(request);
        // AccessToken 유효성 검사
        if (StringUtils.hasText(jwt)) {
            //토큰의 유효성 검사
            JWTTokenDTO JWTTokenDTO = jwtProvider.validateToken(jwt);

            logger.info("CustomAuthorizationFilter token username:" + JWTTokenDTO.getUsername());
            logger.info("CustomAuthorizationFilter token userId:" + JWTTokenDTO.getUserId());

            // 유효한 토큰인 경우 사용자 정보 및 권한(현재없음)을 조회하여 securityContext에 정보를 저장한다.

            // 토큰으로 인증 정보를 추출
            Authentication authentication = new CustomAuthenticationToken(JWTTokenDTO.getUserId(), JWTTokenDTO.getUsername(), null);
            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}