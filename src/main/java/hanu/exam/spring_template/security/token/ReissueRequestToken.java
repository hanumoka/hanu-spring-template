package hanu.exam.spring_template.security.token;

import lombok.Builder;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 토큰 재발행 요청시 생성되는 토큰
 */
public class ReissueRequestToken extends AbstractAuthenticationToken {

    private String username;
    private Long userId;
    private String refreshToken; // 필요없지만 혹시나...

//    @Builder
//    public ReissueRequestToken(String username, Long userId) {
//        super(null);
//        this.username = username;
//        this.userId = userId;
//    }

    @Builder
    public ReissueRequestToken(String username, Long userId, String refreshToken) {
        super(null);
        this.username = username;
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
