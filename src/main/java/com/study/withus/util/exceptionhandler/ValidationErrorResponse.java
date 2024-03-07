package com.study.withus.util.exceptionhandler;

import lombok.Data;

@Data
public class ValidationErrorResponse {
    private final String fieldName;
    private final String msg;
}
