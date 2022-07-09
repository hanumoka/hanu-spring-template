package hanu.exam.springtestexam.security;

import hanu.exam.springtestexam.security.service.AccountContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * AuthenticationFilter에 의해 로그인 인증을 시도하는 주체
 */
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

        //인증을 위한 구현 로직이 들어간다.
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, accountContext.getAccount().getPassword())){
            //패스워드 검증
            throw new BadCredentialsException("BadCredentialsException");
        }

        // 패스워드 말고도 필요한 검증을 이곳에서 처리하면 된다.
        // ex. 계정 lock, 비밀번호 만료 등등...

        //검증이 성공한 검증정보를 authenticationManager에게 다시 리턴한다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountContext.getAccount().getUsername(),
                        null, accountContext.getAuthorities());

        return authenticationToken;
    }

    /**
     * provider의 동작 여부를 설정
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}