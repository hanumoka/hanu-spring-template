package hanu.exam.springtestexam.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDTO {
    private String username;  // 회원이 입력하는 유니크키, 이메일이나 전화번호가 될수 있다. security 컨벤션때문에 usernmae으로 통일한다.
    private String password;
    private String email;
    private String name;
}
