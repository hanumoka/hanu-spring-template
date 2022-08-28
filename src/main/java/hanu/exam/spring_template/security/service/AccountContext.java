package hanu.exam.spring_template.security.service;

import hanu.exam.spring_template.domain.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Account 엔티티가 지저분해지는 것을 막기 위한 껍대기 클래스
 * 계정관리에 필요한 보안기능은 User에서 확장해서 사용하면 된다.
 */
public class AccountContext extends User {

    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
