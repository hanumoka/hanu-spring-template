package hanu.exam.spring_template.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.security.token.JwtRequestToken;
import hanu.exam.spring_template.security.jwt.JwtTokenDto;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 해당 토큰은 request에서 jwt 토큰의 유효성을 검사하고, 유요한 토큰이 있는경우 해당 토큰정보를 CustomAuthenticationToken 로 변환하여
 * authenticationProviderImpl에게 처리를 위임 한다.
 *
 * 유효한 토큰이 없는 경우 ===> AuthenticationEntryPointImpl로 넘어간다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    /**
     * 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
     * 주의!
     * - 이 필터는 security에서 검증할 데이터를 수집하는 필터이다.(header의 accesstoken정보)
     * - 실질적인 검증 기능을 본 필터에서 하면 안된다. => AuthenticationProviderImpl에게 넘기자.
     * - DB같은 곳에 조회 같은것을 하지 말자. 이녀석은 문지기
     * 주된 처리
     * -
     *
     * 주의: 이곳에서 예외를 던져도 AuthenticationEntiryPoint에서 잡지를 못한다.
     * 데이터를 request에 담아서 전달하자.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.warn("JwtTokenFilter doFilterInternal");

        // Request Header에서 토큰 추출
        String accessToken = jwtProvider.resolveAccessToken(request);
        log.info("accessToken:" + accessToken);

        // AccessToken 유효성 검사
        if (StringUtils.hasText(accessToken)) {
            JwtTokenDto jwtTokenDto;

            try{
                //토큰의 유효성 검사
                jwtTokenDto = jwtProvider.validateAccessToken(accessToken);

                // 토큰으로 인증 정보를 추출
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                JwtRequestToken jwtRequestToken = new JwtRequestToken(
                        jwtTokenDto.getUserId(),
                        jwtTokenDto.getUsername(),
                        authorities);

                // SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(jwtRequestToken);

            }
            catch(TokenExpiredException tee){
                log.error("================================================");
                log.error("JwtTokenFilter -TokenExpiredException doFilterInternal() 오류발생");
                log.error("token : {}", accessToken);
                log.error("Exception Message : {}", tee.getMessage());
                log.error("Exception StackTrace : {");
                tee.printStackTrace();
                log.error("}");
                log.error("================================================");
                request.setAttribute("exception", ErrorCode.JWT_EXPIRED_ACCESS_TOKEN);
            }
            catch(Exception e){
                log.error("================================================");
                log.error("JwtTokenFilter -exception doFilterInternal() 오류발생");
                log.error("token : {}", accessToken);
                log.error("Exception Message : {}", e.getMessage());
                log.error("Exception StackTrace : {");
                e.printStackTrace();
                log.error("}");
                log.error("================================================");
                request.setAttribute("exception", ErrorCode.UNKNOWN_SERVER_ERROR);
            } // catch
        } //if
        else{
            log.warn("accessToken 없음...");
            request.setAttribute("exception", ErrorCode.JWT_NOT_EXIST_ACCESS_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

}