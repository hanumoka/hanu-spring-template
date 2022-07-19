package hanu.exam.springtestexam.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 주의 로그인 실패시 : AuthenticationServiceException 를 던저야
 * AuthenticationFailureHandlerImpl 가 동작한다.
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username;
        String password;

        try {
            TypeReference<Map<String, Object>> ref = new TypeReference<Map<String, Object>>() {};
            Map<String, Object> requestMap = new ObjectMapper().readValue(request.getInputStream(), ref);
            username = requestMap.get("username").toString();
            password = requestMap.get("password").toString();

        } catch (IOException e) {
            //TODO: username, password가 필수 입력값 이라는 응답을 명시적으로 해줄 필요가 있다.
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}