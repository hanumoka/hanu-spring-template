package hanu.exam.spring_template.exception;

import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.common.response.ComErrorResponse;
import hanu.exam.spring_template.exception.auth.CustomAuthException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ComErrorResponse> handleException(Exception e,
                                                               HandlerMethod handlerMethod,
                                                               HttpServletRequest request) {
        log.error("handleException", e);
        String controllerName = handlerMethod.getMethod().getDeclaringClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String path = request.getRequestURI();

        final ComErrorResponse response = ComErrorResponse
                .of(ErrorCode.INTERNAL_SERVER_ERROR, controllerName, methodName, path);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomAuthException.class)
    protected ResponseEntity<ComErrorResponse> handleCustomAuthException(CustomAuthException e,
                                                                         HandlerMethod handlerMethod,
                                                                         HttpServletRequest request) {
        log.error("handleCustomAuthException", e);
        String controllerName = handlerMethod.getMethod().getDeclaringClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String path = request.getRequestURI();

        final ComErrorResponse response = ComErrorResponse
                .of(ErrorCode.INTERNAL_SERVER_ERROR, controllerName, methodName, path);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ComErrorResponse> handleBusinessException(final BusinessException e,
                                                                       HandlerMethod handlerMethod,
                                                                       HttpServletRequest request) {
        log.warn("handleBusinessException", e);
        String controllerName = handlerMethod.getMethod().getDeclaringClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String path = request.getRequestURI();

        final ErrorCode errorCode = e.getErrorCode();
        final ComErrorResponse response = ComErrorResponse
                .of(errorCode, controllerName, methodName, path);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }



//    /**
//     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
//     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
//     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error("handleMethodArgumentNotValidException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
//     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
//     */
//    @ExceptionHandler(BindException.class)
//    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
//        log.error("handleBindException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * enum type 일치하지 않아 binding 못할 경우 발생
//     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
//     */
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
//        log.error("handleMethodArgumentTypeMismatchException", e);
//        final ErrorResponse response = ErrorResponse.of(e);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * 지원하지 않은 HTTP method 호출 할 경우 발생
//     */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        log.error("handleHttpRequestMethodNotSupportedException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
//        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
//    }
//
//    /**
//     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
//        log.error("handleAccessDeniedException", e);
//        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
//    }
//
//    @ExceptionHandler(BusinessException.class)
//    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
//        log.error("handleEntityNotFoundException", e);
//        final ErrorCode errorCode = e.getErrorCode();
//        final ErrorResponse response = ErrorResponse.of(errorCode);
//        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
//    }


}
