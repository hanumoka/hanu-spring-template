package hanu.exam.springtestexam.security.dto;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReIssueReqDto {
    private String refreshToken;
}
