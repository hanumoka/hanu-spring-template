package hanu.exam.spring_template.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.security.jwt.JwtProvider;
import hanu.exam.spring_template.security.jwt.JwtTokenDto;
import hanu.exam.spring_template.security.token.ReissueRequestToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 토큰 재발행 요청을 잡는 필터
 */
@Slf4j
public class ReissueTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public ReissueTokenFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request
            , HttpServletResponse response) throws AuthenticationException {
        log.info("ReissueTokenFilter attemptAuthentication...");

        //쿠키에서 리프레시토큰 추출
        String refreshToken = jwtProvider.resolveRefreshTokenInCookie(request);
        log.info("refreshToken : {}", refreshToken);

        ReissueRequestToken reissueRequestToken;
        //refreshToken 벨리데이션 체크
        try{
            JwtTokenDto jwtTokenDto = jwtProvider.validateRefreshToken(refreshToken);
            log.info(jwtTokenDto.toString());

            //TODO: ReissueToken
            reissueRequestToken = ReissueRequestToken.builder()
                    .refreshToken(refreshToken)
                    .userId(jwtTokenDto.getUserId())
                    .userName(jwtTokenDto.getUsername())
                    .build();

        }catch(TokenExpiredException tee){
            request.setAttribute("exception", ErrorCode.JWT_EXPIRED_REFRESH_TOKEN);
            throw tee;
        }catch(Exception e){
            request.setAttribute("exception", ErrorCode.UNKNOWN_SERVER_ERROR.getCode());
            throw e;
        }
        return this.getAuthenticationManager().authenticate(reissueRequestToken);
    }
}
