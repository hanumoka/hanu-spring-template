package hanu.exam.spring_template.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.exception.auth.InvalidLoginInfoException;
import hanu.exam.spring_template.security.dto.LoginReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 주의 로그인 실패시 : AuthenticationServiceException 를 던저야
 * AuthenticationFailureHandlerImpl 가 동작한다.
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginReqDto loginDTO = checkLoginParam(request);

        //TODO: 삭제해야한다.
        log.debug("username = " + loginDTO.getUsername());
        log.debug("password = " + loginDTO.getPassword());

        //실제 해당 로그인 정보가 유효한지는 Authentication Provider manager에게 위임한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 로그인 파라미터가 형식상 유효한지만 검사한다.
     */
    private LoginReqDto checkLoginParam(HttpServletRequest request){

        LoginReqDto loginReqDto = null;
        try {
            loginReqDto = objectMapper.readValue(request.getInputStream(), LoginReqDto.class);
        } catch (IOException e) {
            throw new InvalidLoginInfoException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        if(!StringUtils.hasText(loginReqDto.getUsername()) ||
        !StringUtils.hasText(loginReqDto.getPassword())){
            throw new InvalidLoginInfoException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        //MEMO: 추가적인 username 형식검사나, 패스워드 페턴검사를 할 수도 있다.
        return loginReqDto;
    }

}