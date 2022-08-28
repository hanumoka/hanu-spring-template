package hanu.exam.spring_template.common;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 쿠키 처리
 */
public class HttpUtil {

    /**
     * 리프레시 토큰을 쿠키에 저장한다.
     */
    public static HttpServletResponse setRefreshToken(HttpServletResponse response
            , String refreshToken
            , long maxAgeInSecond) {
        // TODO : https 적용시 secure 적용 필요
        ResponseCookie cookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .sameSite("lax")
                .maxAge(maxAgeInSecond)
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return response;
    }

    /**
     * 쿠키에서 리프레시토큰을 추출한다.
     */
    public static String getRefreshToken(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh-token")) {
                return cookie.getValue();
            }
        }
        return null;
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
