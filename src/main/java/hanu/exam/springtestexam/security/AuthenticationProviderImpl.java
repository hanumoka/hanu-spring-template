package hanu.exam.springtestexam.security;

import hanu.exam.springtestexam.security.jwt.CustomAuthenticationToken;
import hanu.exam.springtestexam.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * AuthenticationFilter에 의해 로그인 인증을 시도하는 주체
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증 구현
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //파라미터 authentication 는 AuthenticationManager로 부터 전달 받는다.
        //authentication 에는 사용자가 입력한 인증정보(username, password)가 들어있다.

        log.info("=========================================>");
        log.info("AuthenticationProviderImpl authenticate...");

        //인증을 위한 구현 로직이 들어간다.
        String username = authentication.getName();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        if(authentication instanceof CustomAuthenticationToken){
            log.info("jwt 토큰을 이용한 request의 인증 요청...");
        }else if(authentication instanceof UsernamePasswordAuthenticationToken){
            log.info("/login 엔드포인트의 로그인 요청...");
            String password = (String)authentication.getCredentials();
            if(!passwordEncoder.matches(password, accountContext.getAccount().getPassword())){
                //패스워드 검증
                throw new BadCredentialsException("BadCredentialsException");
            }
        }

        // 패스워드 말고도 필요한 검증을 이곳에서 처리하면 된다.
        // ex. 계정 lock, 비밀번호 만료, 접근하려는 리소스의 권한을 가지고 있는지 등접

        //검증이 성공한 검증정보를 authenticationManager에게 다시 리턴한다.
        return new CustomAuthenticationToken(accountContext.getAccount().getId(),
                accountContext.getAccount().getUsername(),
                accountContext.getAuthorities());
    }

    /**
     * provider의 동작 여부를 설정
     * supports 메소드를 통해 해당 AuthenticationProvider가 지원하는 인증 타입인지 확인합니다.
     */
    @Override
    public boolean supports(Class<?> authentication) {

        log.info("supports");

        /**
         * /login의 로그인 요청인 경우 인가시도
         */
        if(authentication.equals(UsernamePasswordAuthenticationToken.class)) return true;

        /**
         * jwt 토큰을 가진 요청인 경우 인가시도
         */
        if(CustomAuthenticationToken.class.isAssignableFrom(authentication)) return true;

        return false;

    }

}