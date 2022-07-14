package hanu.exam.springtestexam.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTTokenDTO {
    private Long userId;
    private String username;

    private JWTTokenDTO(){}
}
