package hanu.exam.spring_template.exception.entity_not_found;

import hanu.exam.spring_template.common.response.ErrorCode;
import hanu.exam.spring_template.exception.BusinessException;

public abstract class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public EntityNotFoundException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public EntityNotFoundException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }
}
