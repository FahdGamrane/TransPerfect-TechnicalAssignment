package com.project.recommendations.exceptions;


import com.project.recommendations.holders.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public final ErrorResponse handleApiBusinessException(BusinessException exception) {
        return ErrorResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }


}
