package hanu.exam.spring_template.old.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    //enum으로 api 요청 결과에 대한 code, message를 정의한다.
    @AllArgsConstructor
    @Getter
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String msg;
    }

    //단일건 결과를 처리하는 메소드
    public <T> CommonResult<T> getCommonResult(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    //결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult<?> result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    //성공 결과만 처리하는 메소드
    public CommonResult<?> getSuccessResult() {
        CommonResult<?> result = new CommonResult<>();
        setSuccessResult(result);
        return result;
    }

    private void setFailResult(CommonResult<?> result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }

    //실패 결과만 처리하는 메소드
    public CommonResult<?> getFailResult() {
        CommonResult<?> result = new CommonResult<>();
        setFailResult(result);
        return result;
    }

    public CommonResult<?> getFailResult(String msg) {
        CommonResult<?> result = new CommonResult<>();
        setFailResult(result);
        result.setMsg(msg);
        return result;
    }

    //실패 결과만 처리하는 메소드
    public CommonResult<?> getFailResult(int code, String msg) {
        CommonResult<?> result = new CommonResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    //실패 결과만 처리하는 메소드
    public CommonResult<?> getFailResult(int code, String msg, String msgDetail) {
        CommonResult<?> result = new CommonResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setMsgDetail(msgDetail);
        return result;
    }

}