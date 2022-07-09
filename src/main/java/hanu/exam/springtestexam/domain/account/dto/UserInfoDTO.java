package hanu.exam.springtestexam.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoDTO {
    private long id;
    private String userid;
    private String email;
    private String name;
}
