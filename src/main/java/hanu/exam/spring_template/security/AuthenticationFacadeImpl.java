package hanu.exam.spring_template.security;

import hanu.exam.spring_template.domain.account.entity.Account;
import hanu.exam.spring_template.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AccountRepository accountRepository;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public Account getAuthUser() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("userId:"+ userId);
        return accountRepository.findById(userId).orElseThrow(() -> new RuntimeException("로그인 존재하지 않음"));
    }

    @Override
    public Account getAuthUserInPublic() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("userId:"+ userId);
        return accountRepository.findById(userId).orElse(null);
    }
}