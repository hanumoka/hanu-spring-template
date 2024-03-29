package hanu.exam.spring_template.domain.account.repository;

import hanu.exam.spring_template.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findById(long id);
}
