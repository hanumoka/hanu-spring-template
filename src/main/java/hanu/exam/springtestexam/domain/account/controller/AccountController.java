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

    // 1.accesstoken을 기용하여 자신의 정보 조회

    // 2.만료된 accesstoken 인경우 토큰 재발행 (프론트엔드로 부터 refresh token을 받아서)


    //------------ TODO: 아래는 토큰 고도화 내용 나중에 하자.

    // 3.리프레시토큰의 경우 httponly, secure(나중) 쿠키로 업그레이드

    // 4.만료된 accesstoken요청이 온경우 쿠키에서 refresh토큰을 가져와서 accesstoken 발행

    // 5.로그아웃과 서버에서 사용자 강제 로그아웃 기능을 위한 기능 고도화
    //  - 레디스에 저장한다.
    //  -- active accesstoken = 키(accesstoken) : 값(refreshtoken) 발급한 토큰 저장(자동으로 만료된 토큰은 삭제)
    //  -- active refreshtoken = 키(refreshtoken) :  발급한 리프리시토큰 저장(자동으로 만료된 토큰은 삭제)


//    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
//    public DataApiResponse<UserInfoDTO> info(@PathVariable long id) {
//        UserInfoDTO userInfoDTO = accountService.getInfo(id);
//        return new DataApiResponse<>(userInfoDTO);
//    }
}
