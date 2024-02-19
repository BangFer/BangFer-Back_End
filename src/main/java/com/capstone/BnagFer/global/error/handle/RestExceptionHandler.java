package com.capstone.BnagFer.global.error.handle;

import com.capstone.BnagFer.global.error.ErrorResponse;
import com.capstone.BnagFer.global.error.exception.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleException(RestException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = ErrorResponse.from(httpStatus);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
