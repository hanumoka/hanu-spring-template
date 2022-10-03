package hanu.exam.spring_template.domain.account.controller;

import hanu.exam.spring_template.annotation.RestControllerV1;
import hanu.exam.spring_template.common.response.ApiResponse;
import hanu.exam.spring_template.domain.account.dto.UserInfoDTO;
import hanu.exam.spring_template.domain.account.entity.Account;
import hanu.exam.spring_template.domain.account.service.AccountService;
import hanu.exam.spring_template.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerV1
public class AccountController {

    private static final String MAIN_PATH = "/account";

    private final AccountService accountService;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping(name="사용자가 자신의 정보 조회", value =MAIN_PATH + "/info")
    public ApiResponse<UserInfoDTO> getMyAccountInfo(){
        log.info("getMyAccountInfo...");
        System.out.println("username:" + authenticationFacade.getAuthUser().getUsername());
        Account account = authenticationFacade.getAuthUser();
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .id(account.getId())
                .email(account.getUsername())
                .build();
        return new ApiResponse<UserInfoDTO>(userInfoDTO);
    }

    @GetMapping(name="사용자가 자신의 정보 조회", value =MAIN_PATH + "/test")
    public ApiResponse<String> getTest(){
        log.info("getTest...");
        return new ApiResponse<String>();
    }


}
