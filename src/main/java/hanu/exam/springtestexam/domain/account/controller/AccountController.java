package hanu.exam.springtestexam.domain.account.controller;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.domain.account.dto.UserJoinDTO;
import hanu.exam.springtestexam.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping(name = "회원가입", value = "/signup")
    public ApiResponse join(@RequestBody UserJoinDTO userJoinDTO) {
        accountService.join(userJoinDTO);
        return new ApiResponse();
    }

    //TODO: 로그인은 security에서 필터로처리, but swagger를 위한 가짜 컨트롤러는 필요할듯

    @GetMapping(name="사용자가 자신의 정보 조회", value ="/account")
    public ApiResponse getMyAccountInfo(){
        log.info("getMyAccountInfo...");
        return new ApiResponse();
    }

    //TODO: 엑시스 토큰 만료시 리프레시 토큰 발급 컨트롤러 추가하기


    //TODO: 엑세스토큰을 재발급하는 명시적인 컨트롤러가 필요한가?
    //TODO: 그냥 액세스토큰 검사시 엑세스토큰이 만료되면 자동으로 refresh token을 가져와서 accesstoken을 발급하면 좋지 않은가?
    //TODO: refreshToken을 프론트에 전달하는것은 옳은가?
    //TODO: csrf는 어떤방법으로 해결이 가능하지?
}
