package hanu.exam.springtestexam.security.jwt;

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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-expire-length}")
    private long ACCESS_VALIDITY_IN_MILLISECONDS;

    @Value("${jwt.refresh-token-expire-length}")
    private long REFRESH_VALIDITY_IN_MILLISECONDS;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createAccessToken(Long userId, String username, List<String> roles, String issuer){
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_VALIDITY_IN_MILLISECONDS))
                .withIssuer(issuer)
                .withClaim("username", username)
//                .withClaim("roles", roles)  // 권한은 현재 없음
                .sign(algorithm);
    }

    public String createRefreshToken(Long userId, String username, String issuer){
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_VALIDITY_IN_MILLISECONDS))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public JwtTokenDto validateToken(String token) {

        if(StringUtils.isEmpty(token)){
            throw new JWTVerificationException("액세스 토큰이 존재하지 않습니다.");
        }

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        Claim claim = decodedJWT.getClaim("username");
        return new JwtTokenDto(Long.valueOf(decodedJWT.getSubject()), decodedJWT.getClaim("username").asString());
    }

//    public String getRefreshTokenIdTokenFromAccessToken(String token){
//        DecodedJWT decodedJWT = JWT.decode(token);
//        return decodedJWT.getClaim("refresh_token_id").toString();
//    }

    /**
     * 액세스 토큰만 쿠키로 저장하고, 리프레쉬 토큰은 DB에 저장한다.
     */
//    public HttpServletResponse createCookie(HttpServletResponse response, String token) {
//        // TODO : https 적용시 secure 적용 필요
//        ResponseCookie cookie = ResponseCookie.from(HEADER_NAME, token)
//                .httpOnly(true)
//                .sameSite("lax")
//                .maxAge(ACCESS_VALIDITY_IN_MILLISECONDS)
//                .path("/")
//                .build();
//        response.addHeader("Set-Cookie", cookie.toString());
//        return response;
//    }
//
//    public HttpServletResponse deleteCookie(HttpServletResponse response) {
//        // TODO : https 적용시 secure 적용 필요
//        ResponseCookie cookie = ResponseCookie.from(HEADER_NAME, null)
//                .httpOnly(true)
//                .sameSite("lax")
//                .maxAge(0)
//                .path("/")
//                .build();
//        response.addHeader("Set-Cookie", cookie.toString());
//        return response;
//    }

//    public String resolveCookie(HttpServletRequest request) {
//        final Cookie[] cookies = request.getCookies();
//        if (cookies == null) return null;
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(HEADER_NAME)) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }

//    @Transactional
//    public RefreshToken saveRefreshToken(String refreshToken) {
//        return refreshTokenRepo.save(RefreshToken.builder().token(refreshToken).build());
//    }

}