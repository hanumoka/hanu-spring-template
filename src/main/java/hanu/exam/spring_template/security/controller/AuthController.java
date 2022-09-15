package hanu.exam.spring_template.security.controller;

import hanu.exam.spring_template.annotation.RestControllerV1;
import hanu.exam.spring_template.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@RestControllerV1
public class AuthController {

    private static final String MAIN_PATH = "/auth";

    // 로그인, 토큰 재발행 같은 인증 인가 같은 로직은 도메인에 두지 말고 그냥 security에 숨겨두는 것이 좋을것 같다.
    // 스웨거가 필요하다면 껍데기 컨트롤러를 만들자.
    //만료된 accessToken과 만료되지 않은 refreshToken이 있어야 동작한다.
    @PostMapping(name="액세스토큰 리프레시토큰 재발급", value=MAIN_PATH + "/reissue")
    public ResponseEntity<ApiResponse> reIssueTokens(){
        log.info("reIssueTokens");
        /**
         * accessToken이 만료가 된 경우에만 토큰들을 재발행한다.
         * accessToken이 만료전이라면 아직 만료된 토큰이 아니라는 응답을 해준다.
         * 나중에 refreshToken을 쿠키로 내려주면 해당 로직을 개선해야 한다.
         */

        //헤더의 엑세스토큰아 만료되었는지 확인
        //쿠키에 리프레시토큰이 유효한지 확인
        //엑세스토큰 재발행, 리프레시토큰 쿠키에 다시 셋팅

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse());
    }
}
