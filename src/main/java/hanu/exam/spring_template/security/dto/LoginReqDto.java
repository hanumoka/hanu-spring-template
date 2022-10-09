package hanu.exam.spring_template.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "로그인 요청 DTO")
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    @Schema(description = "이메일 형식", name="username")
    private String username;
    @Schema(description = "패스워드")
    private String password;
}
