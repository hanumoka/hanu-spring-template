package hanu.exam.springtestexam.security.handler;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.common.ApiResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security에서 403 인가실패를 처리하는 헨들러(인증이 아니라 인가중 실패 처리)
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        /**
         * 이곳에서 403 응답을 만들면 된다.
         */
        log.debug("AccessDeniedHandlerImpl hanlde...");
        ApiResponse.error(response, HttpStatus.UNAUTHORIZED, ApiResponseType.UNAUTHORIZED_RESPONSE);
    }

}
