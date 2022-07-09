package hanu.exam.springtestexam.domain.account.controller;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.domain.account.dto.UserJoinDTO;
import hanu.exam.springtestexam.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping(name = "회원가입", value = "/user")
    public ApiResponse join(@RequestBody UserJoinDTO userJoinDTO) {
        accountService.join(userJoinDTO);
        return new ApiResponse();
    }

//    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
//    public DataApiResponse<UserInfoDTO> info(@PathVariable long id) {
//        UserInfoDTO userInfoDTO = accountService.getInfo(id);
//        return new DataApiResponse<>(userInfoDTO);
//    }
}
