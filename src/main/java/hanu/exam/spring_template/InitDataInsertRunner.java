package hanu.exam.spring_template;

import hanu.exam.spring_template.domain.account.dto.UserJoinDTO;
import hanu.exam.spring_template.domain.account.service.AccountService;
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

        UserJoinDTO userJoinDTO1 = new UserJoinDTO("test1", "1234","test@test.com", "kyb");
        accountService.join(userJoinDTO1);
    }
}
