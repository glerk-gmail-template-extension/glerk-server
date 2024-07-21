package com.glerk.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed. \nPlease try again later."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token."),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "User already exists. \nPlease log in."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found. \nPlease sign up."),
    GOOGLE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Google user not found.");

    private final HttpStatus httpStatus;
    private final String message;
}
