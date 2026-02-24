package com.dekk.common.error;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "EU001", "이미 온보딩이 완료된 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
