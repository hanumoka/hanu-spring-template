package hanu.exam.springtestexam;

import hanu.exam.springtestexam.domain.Account;
import hanu.exam.springtestexam.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 기초데이터 insert 러너
@RequiredArgsConstructor
@Component
public class InitDataInsertRunner implements CommandLineRunner {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 서버 시작시 기초 권한과 계정을 db에 저장

        Account account = Account.builder()
                .username("test1")
                .password(passwordEncoder.encode("1234"))
                .build();

        accountService.createAccount(account);
    }
}
