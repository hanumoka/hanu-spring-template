package hanu.exam.spring_template.security.filter;

import hanu.exam.spring_template.security.jwt.JwtProvider;
import hanu.exam.spring_template.security.jwt.JwtTokenDto;
import hanu.exam.spring_template.security.token.ReissueRequestToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        String refreshToken = jwtProvider.resolveRefreshTokenInCookie(request);
//        //refreshToken 벨리데이션 체크
        JwtTokenDto jwtTokenDto = jwtProvider.validateRefreshToken(refreshToken);

        log.info(jwtTokenDto.toString());

        //TODO: ReissueToken
        ReissueRequestToken reissueRequestToken = ReissueRequestToken.builder()
                .refreshToken(refreshToken)
                .userId(jwtTokenDto.getUserId())
                .userName(jwtTokenDto.getUsername())
                .build();
//
//        System.out.println("====================jwtTokenDto=======================");
//        System.out.println(jwtTokenDto);
//
//        accessToken = jwtProvider.setRefreshTokenInCookie(jwtTokenDto.getUserId(), jwtTokenDto.getUsername(), response);
//        ApiResponse.accessToken(response, accessToken);

//        return super.attemptAuthentication(request, response);
        return this.getAuthenticationManager().authenticate(reissueRequestToken);
    }
}
