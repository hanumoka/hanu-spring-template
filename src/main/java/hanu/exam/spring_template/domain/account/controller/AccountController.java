package hanu.exam.spring_template.domain.account.controller;

import hanu.exam.spring_template.annotation.RestControllerV1;
import hanu.exam.spring_template.common.response.ApiResponse;
import hanu.exam.spring_template.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerV1
public class AccountController {

    private static final String MAIN_PATH = "/account";

    private final AccountService accountService;

    @GetMapping(name="사용자가 자신의 정보 조회", value ="/account")
    public ApiResponse<String> getMyAccountInfo(){
        log.info("getMyAccountInfo...");
        return new ApiResponse<String>();
    }

}
