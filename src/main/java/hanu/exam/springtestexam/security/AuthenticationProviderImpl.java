package hanu.exam.springtestexam.security;

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
        String password = (String)authentication.getCredentials();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

//        if(!passwordEncoder.matches(password, accountContext.getAccount().getPassword())){
//            //패스워드 검증
//            throw new BadCredentialsException("BadCredentialsException");
//        }

        // 패스워드 말고도 필요한 검증을 이곳에서 처리하면 된다.
        // ex. 계정 lock, 비밀번호 만료 등등...

        //검증이 성공한 검증정보를 authenticationManager에게 다시 리턴한다.
        return new CustomAuthenticationToken(accountContext.getAccount().getId(), accountContext.getAccount().getUsername(), accountContext.getAuthorities());
    }

    /**
     * provider의 동작 여부를 설정
     * supports 메소드를 통해 해당 AuthenticationProvider가 지원하는 인증 타입인지 확인합니다.
     * -> 요청에 인증타입을 확인?
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // TODO: CustomAuthorizationFilter doFilter -> 이곳으로 온다.
        // 로그인 요청도 이걸 탄아 supports -> AuthenticationProviderImpl authenticate...
        // 실제 로그인 처리 직전을 거친다.

        // TODO: /login 오는 요청타입을 검사
        // authentication.equals(UsernamePasswordAuthenticationToken.class);

        // TODO: /일반적인 jwt 토큰요텅 타입 검사
        // CustomAuthenticationToken.class.isAssignableFrom(authentication);
        // 이 메소드의 역할은?
        log.info("supports");

        if(authentication.equals(UsernamePasswordAuthenticationToken.class)) return true;

        if(CustomAuthenticationToken.class.isAssignableFrom(authentication)) return true;

        return false;

    }

}