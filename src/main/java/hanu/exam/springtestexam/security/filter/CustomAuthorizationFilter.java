package hanu.exam.springtestexam.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.springtestexam.common.ErrorCode;
import hanu.exam.springtestexam.exception.auth.ExpiredRefreshTokenException;
import hanu.exam.springtestexam.exception.auth.InValidReIssueInfoException;
import hanu.exam.springtestexam.exception.auth.InvalidLoginInfoException;
import hanu.exam.springtestexam.exception.auth.InvalidRefreshTokenException;
import hanu.exam.springtestexam.security.dto.LoginReqDto;
import hanu.exam.springtestexam.security.dto.ReIssueReqDto;
import hanu.exam.springtestexam.security.token.CustomAuthenticationToken;
import hanu.exam.springtestexam.security.jwt.JwtTokenDto;
import hanu.exam.springtestexam.security.jwt.JwtProvider;
import hanu.exam.springtestexam.security.token.ReissueRequestToken;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

        // Request Header에서 토큰 추출
        String jwt = resolveAccessToken(request);
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

                /**
                 * 토큰 재발행 요청인경우
                 * - POST
                 * - uri : /reissue
                 * - request_body
                 *  - refreshToken: 리프레쉬 토큰이 있고 이 토큰이 유요현경우
                 */
                //1.요청 URI가 /reissue 인경우 패스
                if("/reissue".equals(request.getRequestURI())
                        && "POST".equals(request.getMethod())){
                    logger.info("토큰 재발행 요청 확인...");
                    Authentication authentication = resolveAndValidateRefreshToken(request, response);

                    // SecurityContext에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }else{
                    throw tee;
                }

            } catch(Exception e){
                logger.info("accessToken 벨리데이션 예외발생");
                throw e;
            } // catch

        }

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 추출
     */
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_NAME)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Request에서 RefreshToken 추출
     */
    private ReissueRequestToken resolveAndValidateRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ReIssueReqDto reIssueReqDto = null;
        try {
            reIssueReqDto = objectMapper.readValue(request.getInputStream(), ReIssueReqDto.class);
            logger.info("refreshToken:"+ reIssueReqDto.getRefreshToken());
            JwtTokenDto jwtTokenDto = jwtProvider.validateRefreshToken(reIssueReqDto.getRefreshToken());
//            logger.info("jwtTokenDto1:"+ jwtTokenDto1.toString());
            return ReissueRequestToken.builder()
                    .username(jwtTokenDto.getUsername())
                    .userId(jwtTokenDto.getUserId())
                    .refreshToken(reIssueReqDto.getRefreshToken())
                    .build();
        } catch (IOException e) {
            //리프레시토큰이 요청파라미터가 잘못됨 => 405
            throw new InValidReIssueInfoException(ErrorCode.REISSUE_INPUT_INVALID);
        }catch(TokenExpiredException e2){
          // 리프레시토큰이 만료된 경우 => 로그아웃 처리 401 응답
            throw new ExpiredRefreshTokenException(ErrorCode.JWT_EXPIRED_ACCESS_TOKEN);
        } catch(JWTVerificationException e3){
            //형식이 잘못된 리프레시토큰 => 401
            response.setStatus(UNAUTHORIZED.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "리프레시토큰 벨리데이션 오류");
            response.setContentType(APPLICATION_JSON_VALUE);
//            jwtCookieProvider.deleteCookie(response);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
//            throw new InvalidRefreshTokenException(ErrorCode.REFRESH_TOKEN_INVALID);

        }

        return null;

    }

}