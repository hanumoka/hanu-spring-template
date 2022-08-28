package hanu.exam.springtestexam.common;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * 쿠키 처리
 */
public class HttpUtil {

    /**
     * 액세스 토큰만 쿠키로 저장하고, 리프레쉬 토큰은 DB에 저장한다.
     */
    public static HttpServletResponse setRefreshTokenCookie(HttpServletResponse response
            , String refreshToken
            , long maxAgeInSecond) {
        // TODO : https 적용시 secure 적용 필요
        ResponseCookie cookie = ResponseCookie.from("refersh-token", refreshToken)
                .httpOnly(true)
                .sameSite("lax")
                .maxAge(maxAgeInSecond)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return response;
    }

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

}
