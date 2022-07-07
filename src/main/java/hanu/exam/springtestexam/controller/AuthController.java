package hanu.exam.springtestexam.controller;

import hanu.exam.springtestexam.response.CommonResult;
import hanu.exam.springtestexam.response.ResponseService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final ResponseService responseService;

    @PostMapping("/signin")
    public String signIn(){
        return "signin";
    }


    @PostMapping("/signout")
    public String signOut(){
        return "logout";
    }

    @PostMapping("/signup")
    public CommonResult<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
//        userService.signUp(signUpDto.getUsername(), signUpDto.getPassword(), signUpDto.getPasswordRepeat());
        return responseService.getSuccessResult();
    }





}


//@ApiModel(value = "회원가입 파라미터", description = "이메일, 비밀번호, 확인용 비밀번호")
@Data
class SignUpDto {

//    @ApiModelProperty(
//            name = "username"
//            , example = "test@test.com"
//            , required = true
//    )
//    @Email(message = "username은 이메일형식입니다.")
//    @NotBlank(message = "username은 필수값입니다.")
    private String username;

//    @ApiModelProperty(
//            name = "password"
//            , example = "test1234"
//            , required = true
//    )
//    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$", message = "password: 8 ~ 16자 영문, 숫자 조합의 비밀번호를 입력해주세요.")
//    @NotBlank(message = "password1은 필수값입니다.")
    private String password;

//    @ApiModelProperty(
//            name = "passwordRepeat"
//            , example = "test1234"
//            , required = true
//    )
//    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$", message = "passwordRepeat: 8 ~ 16자 영문, 숫자 조합의 비밀번호를 입력해주세요.")
//    @NotBlank(message = "passwordRepeat 필수값입니다.")
    private String passwordRepeat;

}