package hanu.exam.springtestexam.domain.account.repository;

import hanu.exam.springtestexam.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findById(long id);
}
