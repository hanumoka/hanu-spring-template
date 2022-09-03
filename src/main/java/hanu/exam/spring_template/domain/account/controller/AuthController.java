package hanu.exam.spring_template.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * 권한 관련 처리를 하는 컨트롤러, Account분리 하지만 밀접관 관계가 있으므로 동일한 페키지에서 관리한다.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
}
