package hanu.exam.spring_template.domain.account.controller;

import hanu.exam.spring_template.annotation.RestControllerV1;
import hanu.exam.spring_template.common.ApiResponse;
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

    @GetMapping(name="사용자가 자신의 정보 조회", value =MAIN_PATH + "/test")
    public ApiResponse test(){
        log.info("test...");
        return new ApiResponse();
    }

    @GetMapping(name="사용자가 자신의 정보 조회", value ="/account")
    public ApiResponse getMyAccountInfo(){
        log.info("getMyAccountInfo...");
        return new ApiResponse();
    }

//    @PostMapping(name = "회원가입", value = "/signup")
//    public ApiResponse join(@RequestBody UserJoinDTO userJoinDTO) {
//        accountService.join(userJoinDTO);
//        return new ApiResponse();
//    }



    // 로그인, 토큰 재발행 같은 인증 인가 같은 로직은 도메인에 두지 말고 그냥 security에 숨겨두는 것이 좋을것 같다.
    // 스웨거가 필요하다면 껍데기 컨트롤러를 만들자.
//    //만료된 accessToken과 만료되지 않은 refreshToken이 있어야 동작한다.
//    @PostMapping(name="액세스토큰 리프레시토큰 재발급", value="/reissue")
//    public ResponseEntity<ApiResponse> reIssueTokens(){
//        log.info("reIssueTokens");
//        /**
//         * accessToken이 만료가 된 경우에만 토큰들을 재발행한다.
//         * accessToken이 만료전이라면 아직 만료된 토큰이 아니라는 응답을 해준다.
//         * 나중에 refreshToken을 쿠키로 내려주면 해당 로직을 개선해야 한다.
//         */
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse());
//    }
}
