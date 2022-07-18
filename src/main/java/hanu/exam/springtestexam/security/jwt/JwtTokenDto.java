package hanu.exam.springtestexam.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenDto {
    private Long userId;
    private String username;

    private JwtTokenDto(){}
}
