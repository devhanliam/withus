package com.study.withus.util.exceptionhandler;

import com.study.withus.util.exceptionhandler.exception.UserDuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ArrayList<ValidationErrorResponse> errorList = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorList.add(new ValidationErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    protected ResponseEntity handleUserDuplicatedException(UserDuplicatedException ex) {
        ArrayList<ValidationErrorResponse> errorList = new ArrayList<>();
        errorList.add(new ValidationErrorResponse("loginId", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
    }
}
