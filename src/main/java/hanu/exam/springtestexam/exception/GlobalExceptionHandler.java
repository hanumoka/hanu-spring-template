package hanu.exam.springtestexam.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
//    protected ResponseEntity<ErrorResponse> handleDataException() {
//        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
//        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
//    }

//    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
//    protected ResponseEntity<ErrorResponse> handleDataException() {
//        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
//        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
//    }
//
//    @ExceptionHandler(value = { SysException.class })
//    protected ResponseEntity<ErrorResponse> handleCustomException(SysException e) {
//        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
//        return ErrorResponse.toResponseEntity(e.getErrorCode());
//    }
//
//    @ExceptionHandler(value = { Exception.class })
//    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
//        log.error("handleCustomException throw Exception : {}", e.getMessage());
//        e.printStackTrace();
//        return ErrorResponse.toResponseEntity(ErrorCode.SERVER_ERROR);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        log.error("handleCustomException throw handleMethodArgumentNotValid : {}", ex.getMessage());
//        List<String> errorList = ex
//                .getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//        // TODO: 나중에 ex.getLocalizedMessage()는 제거하자. 보안상 좋지 않아 보인다.
//        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorList);
//        return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
//    }
}
