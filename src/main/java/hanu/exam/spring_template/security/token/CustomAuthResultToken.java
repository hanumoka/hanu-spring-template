package hanu.exam.spring_template.security.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Security 인증 결과를 저장하는 토큰
 */
@Getter
public class CustomAuthResultToken extends AbstractAuthenticationToken {

    private Long userId;
    private String username;

    public CustomAuthResultToken(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.username = username;
    }

    public CustomAuthResultToken(Long userId, String username) {
        super(null);
        this.userId = userId;
        this.username = username;
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public CustomAuthResultToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }
}
