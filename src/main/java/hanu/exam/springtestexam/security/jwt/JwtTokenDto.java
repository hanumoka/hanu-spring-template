package hanu.exam.springtestexam.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class JwtTokenDto {
    private Long userId;
    private String username;
    private JwtTokenDto(){}
}
