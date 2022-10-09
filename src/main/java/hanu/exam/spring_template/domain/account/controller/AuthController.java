package hanu.exam.spring_template.domain.account.controller;

import hanu.exam.spring_template.annotation.RestControllerV1;
import hanu.exam.spring_template.common.response.ComApiResponse;
import hanu.exam.spring_template.security.dto.LoginReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * spring security 필터에서 실제 로직을 처리한다.
 * 해당 컨트롤러는 스웨거 api 명세를 출력하는 용도의 껍데기 컨트롤러이다.
 */
//@Tag(name = "auth controller", description = "권한관련 컨트롤러 desc")
@Slf4j
@RequiredArgsConstructor
@RestControllerV1
public class AuthController {

    private static final String MAIN_PATH = "/auth";


    //    @Parameters(value = {
//            @Parameter(name = "username", description = "이메일 계정"),
//            @Parameter(name = "password", description = "비밀번호", in = ParameterIn.QUERY),
//    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = ComApiResponse.class))),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    }
    )
    @Operation(summary = "로그인", description = "로그인", tags = {"권한"})
    @PostMapping(MAIN_PATH + "/login")
    public ComApiResponse<String> login(@RequestBody(description = "로그인 파라미터", required = true,
            content = @Content(schema = @Schema(implementation = LoginReqDto.class))
    ) LoginReqDto loginReqDto) {
        log.info("login...");
        return new ComApiResponse<String>("test");
    }

//    public CommonResult<?> login(@RequestBody @Valid LoginDto loginDto) {
//        // swagger에 표시를 위한 거짓 컨트롤러
//        return responseService.getSuccessResult();
//    }
}
