package hanu.exam.spring_template.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private Long userId;
    private String email;
}
