package hanu.exam.springtestexam.security.dto;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    private String username;
    private String password;
}
