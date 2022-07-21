package hanu.exam.springtestexam.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
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
            Map<String, Object> requestMap = objectMapper.readValue(request.getInputStream(), ref);
            username = requestMap.get("username").toString();
            password = requestMap.get("password").toString();

        } catch (IOException e) {
            //TODO: username, password가 필수 입력값 이라는 응답을 명시적으로 해줄 필요가 있다.
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage(), e);
            // TODO: badRequestException을 만들어서 던지자
            // TODO: 아니면 여기서 예외를 던지지 말고 뒤에 authenticate에서 일괄적으로 처리하는것도 방법일 수 있겠다.
        }

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}