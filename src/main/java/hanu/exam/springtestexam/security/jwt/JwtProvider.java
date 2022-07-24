package hanu.exam.springtestexam.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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

    @Value("${jwt.access-token.secret-key}")
    private String ACCESS_TOKEN_SECRET_KEY;

    @Value("${jwt.refresh-token.secret-key}")
    private String REFRESH_TOKEN_SECRET_KEY;

    @Value("${jwt.access-token.expire-length}")
    private long ACCESS_VALIDITY_IN_MILLISECONDS;

    @Value("${jwt.refresh-token.expire-length}")
    private long REFRESH_VALIDITY_IN_MILLISECONDS;

    @PostConstruct
    protected void init() {
        ACCESS_TOKEN_SECRET_KEY = Base64.getEncoder().encodeToString(ACCESS_TOKEN_SECRET_KEY.getBytes());
    }

    public String createAccessToken(Long userId, String username, String issuer){
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

    public String createRefreshToken(Long userId, String username, String issuer){
        Algorithm algorithm = Algorithm.HMAC256(REFRESH_TOKEN_SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_VALIDITY_IN_MILLISECONDS))
                .withIssuer(issuer)
                .withClaim("username", username)
                .sign(algorithm);
    }

    public JwtTokenDto validateAccessToken(String token) {

        if(StringUtils.isEmpty(token)){
            throw new JWTVerificationException("액세스 토큰이 존재하지 않습니다.");
        }

        Algorithm algorithm = Algorithm.HMAC256(ACCESS_TOKEN_SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        JwtTokenDto jwtTokenDto;

        try{
            decodedJWT = verifier.verify(token);
            Claim claim = decodedJWT.getClaim("username");
        }catch(TokenExpiredException e1){
            log.error("엑세스 토큰 만료...");
            throw e1;
        }
        //SignatureVerificationException
        return jwtTokenDto = new JwtTokenDto(Long.valueOf(decodedJWT.getSubject()), decodedJWT.getClaim("username").asString());
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