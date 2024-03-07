package com.study.withus.util.exceptionhandler.exception;

public class UserDuplicatedException extends RuntimeException{
    public UserDuplicatedException() {
        super("이미 존재하는 아이디 입니다");
    }
}
