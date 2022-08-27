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


    //TODO: 엑세스토큰을 재발급하는 명시적인 컨트롤러가 필요한가?
    //TODO: 그냥 액세스토큰 검사시 엑세스토큰이 만료되면 자동으로 refresh token을 가져와서 accesstoken을 발급하면 좋지 않은가?
    //TODO: refreshToken을 프론트에 전달하는것은 옳은가?
    //TODO: csrf는 어떤방법으로 해결이 가능하지?
}
