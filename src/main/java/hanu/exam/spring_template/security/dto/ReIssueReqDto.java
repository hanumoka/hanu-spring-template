package hanu.exam.spring_template.security.dto;

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
