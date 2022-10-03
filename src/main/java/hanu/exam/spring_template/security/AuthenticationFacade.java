package hanu.exam.spring_template.security;

import hanu.exam.spring_template.domain.account.entity.Account;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    Account getAuthUser();
    Account getAuthUserInPublic();

}

