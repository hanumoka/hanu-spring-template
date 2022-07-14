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
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // Request Header에서 토큰 추출
        String jwt = resolveToken(request);
        // AccessToken 유효성 검사
        if (StringUtils.hasText(jwt)) {

            //토큰의 유효성 검사
            JWTTokenDTO JWTTokenDTO = jwtProvider.validateToken(jwt);  // TODO: header의 accesstoken 검증시 예외처리 (에외처리 전략 추가 필요)
            System.out.println("token username:" + JWTTokenDTO.getUsername());
            System.out.println("token userId:" + JWTTokenDTO.getUserId());

            // 유효한 토큰인 경우 사용자 정보 및 권한(현재없음)을 조회하여 securityContext에 정보를 저장한다.

            // 토큰으로 인증 정보를 추출
            Authentication authentication = new CustomAuthenticationToken(JWTTokenDTO.getUserId(), JWTTokenDTO.getUsername(), null);
            // SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("jwt 실패");
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