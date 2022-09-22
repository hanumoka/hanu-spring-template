package hanu.exam.spring_template.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${hanu.service.name}")
    private String serviceName;
    @Value("${hanu.jwt.access-token.secret-key}")
    private String ACCESS_TOKEN_SECRET_KEY;

    @Value("${hanu.jwt.refresh-token.secret-key}")
    private String REFRESH_TOKEN_SECRET_KEY;

    @Value("${hanu.jwt.access-token.expire-length}")
    private long ACCESS_VALIDITY_IN_MILLISECONDS;

    @Value("${hanu.jwt.refresh-token.expire-length}")
    private long REFRESH_VALIDITY_IN_MILLISECONDS;

    @Value("${hanu.jwt.authorization-header}")
    private String AUTHORIZATION_HEADER;
    @Value("${hanu.jwt.header-name}")
    private String HEADER_NAME;


    @PostConstruct
    protected void init() {
        ACCESS_TOKEN_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_TOKEN_SECRET_KEY.getBytes());
        REFRESH_TOKEN_SECRET_KEY = Base64.getEncoder().encodeToString(REFRESH_TOKEN_SECRET_KEY.getBytes());
    }

    public String createAccessToken(Long userId, String username, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(ACCESS_TOKEN_SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_VALIDITY_IN_MILLISECONDS))
                .withIssuer(issuer)
                .withClaim("username", username)
//                .withClaim("roles", roles)  // 권한은 현재 없음
                //MEMO: 권한 정보를 토큰에 저장하는것이 좋은가? 필터에서 권한을 검사하는것이 더 유연해보인다.
                .sign(algorithm);
    }

    public String createRefreshToken(Long userId, String username, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_VALIDITY_IN_MILLISECONDS))
                .withIssuer(issuer)
                .withClaim("username", username)
                .sign(algorithm);
    }

    public JwtTokenDto validateAccessToken(String accessToken) {

        log.info("validateAccessToken : {}", accessToken);

        if (StringUtils.isEmpty(accessToken)) {
            throw new JWTVerificationException("액세스 토큰이 존재하지 않습니다.");
        }

        Algorithm algorithm = Algorithm.HMAC256(ACCESS_TOKEN_SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        JwtTokenDto jwtTokenDto;

        decodedJWT = verifier.verify(accessToken);
        Claim claim = decodedJWT.getClaim("username");
        //SignatureVerificationException
        return new JwtTokenDto(
                Long.valueOf(decodedJWT.getSubject())
                , decodedJWT.getClaim("username").asString()
        );
    }

    public JwtTokenDto validateRefreshToken(String refreshToken) {

        log.info("validateRefreshToken : {}", refreshToken);

        if (StringUtils.isEmpty(refreshToken)) {
            throw new JWTVerificationException("리프래시토큰이 존재하지 않습니다.");
        }

        Algorithm algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        JwtTokenDto jwtTokenDto;

        decodedJWT = verifier.verify(refreshToken);
        Claim claim = decodedJWT.getClaim("username");
        //SignatureVerificationException
        return new JwtTokenDto(
                Long.valueOf(decodedJWT.getSubject())
                , decodedJWT.getClaim("username").asString()
        );
    }

    /**
     * Request Header에서 accessToken 토큰 추출
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_NAME)) {
            return bearerToken.substring(7);
        } //if
        return null;
    }

    /**
     * 리프래시토큰을 쿠키에 셋팅
     * 1.accessToken 문자열을 리턴
     * 2.Response 쿠키에 리프래시토큰을 셋팅
     */
    public String setRefreshTokenInCookie(Long userId, String userName, HttpServletResponse response) {
        // TODO : https 적용시 secure 적용 필요

        // JWT Token 발급 - accessToken
        String accessToken = createAccessToken(
                userId
                , userName
                , serviceName);

        // JWT Token 발급 - refreshToken
        String refreshToken = createRefreshToken(
                userId
                , userName
                , serviceName);

        ResponseCookie cookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .sameSite("lax")
//                .maxAge(REFRESH_VALIDITY_IN_MILLISECONDS / 1000) // TODO: maxAge를 안줘도 괜찮을듯...
//                .maxAge(REFRESH_VALIDITY_IN_MILLISECONDS + 100000000 ) // 테스트용도.
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return accessToken;
    }

    /**
     * 쿠키에서 리프래치토큰을 추출한다.
     */
    public String resolveRefreshTokenInCookie(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh-token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}