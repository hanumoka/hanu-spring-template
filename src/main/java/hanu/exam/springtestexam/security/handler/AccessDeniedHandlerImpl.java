package hanu.exam.springtestexam.security.handler;

import hanu.exam.springtestexam.common.ApiResponse;
import hanu.exam.springtestexam.common.ApiResponseType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResponse.error(response, ApiResponseType.FORBIDDEN_RESPONSE);
    }

}
