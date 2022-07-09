package hanu.exam.springtestexam.domain.account.service;

import hanu.exam.springtestexam.domain.account.dto.UserInfoDTO;
import hanu.exam.springtestexam.domain.account.dto.UserJoinDTO;
import hanu.exam.springtestexam.domain.account.entity.Account;
import hanu.exam.springtestexam.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(UserJoinDTO userJoinDTO) {
        Account alreadyUser = accountRepository.findByUsername(userJoinDTO.getUsername()).orElse(null);
        if (alreadyUser != null) throw new RuntimeException("이미 등록된 아이디 입니다.");

        Account saveUser = Account.builder()
                .username(userJoinDTO.getUsername())
                .password(passwordEncoder.encode(userJoinDTO.getPassword()))
                .build();

        accountRepository.save(saveUser);
    }

//    @Transactional(readOnly = true)
//    public UserInfoDTO getInfo(long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
//        return UserInfoDTO.builder()
//                .id(user.getId())
//                .userid(user.getLoginid())
//                .email(user.getEmail())
//                .name(user.getName())
//                .build();
//    }

//    public Long createAccount(Account account){
//        Account savedAccount = accountRepository.save(account);
//        return savedAccount.getId();
//    }
}
