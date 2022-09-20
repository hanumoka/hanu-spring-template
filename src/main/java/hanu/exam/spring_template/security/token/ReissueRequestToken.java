package hanu.exam.spring_template.security.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 토큰 재발행 요청시 생성되는 토큰
 */
@Getter
public class ReissueRequestToken extends AbstractAuthenticationToken {

    private String accessToken;  // 필요없으나... 혹시나
    private String refreshToken; // 필요없으나... 혹시나

    private Long userId; // 리프래시토큰에서 추출한 userId
    private String userName; // 리프레시토큰에서 추출한 userName

    @Builder
    public ReissueRequestToken(String accessToken, String refreshToken,
    Long userId, String userName) {
        super(null);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.userName = userName;
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
