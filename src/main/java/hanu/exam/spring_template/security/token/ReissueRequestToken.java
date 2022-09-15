package hanu.exam.spring_template.security.token;

import lombok.Builder;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 토큰 재발행 요청시 생성되는 토큰
 */
public class ReissueRequestToken extends AbstractAuthenticationToken {

    private String accessToken;  // 만료된 액세스토큰
    private String refreshToken; //

    @Builder
    public ReissueRequestToken(String accessToken, String refreshToken) {
        super(null);
        this.accessToken = accessToken;
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

    @Override
    public String toString() {
        return "ReissueRequestToken{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
