package hanu.exam.spring_template.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private long id;
    private String userid;
    private String email;
    private String name;
}
