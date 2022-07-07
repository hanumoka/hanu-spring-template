package hanu.exam.springtestexam.service;

import hanu.exam.springtestexam.domain.Account;
import hanu.exam.springtestexam.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Long createAccount(Account account){
        Account savedAccount = accountRepository.save(account);
        return savedAccount.getId();
    }
}
