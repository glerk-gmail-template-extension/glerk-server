package com.glerk.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "Invalid email format"),
    REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed"),
    SECURITY_EXCEPTION(HttpStatus.UNAUTHORIZED, "Security issue occurred during authentication"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "User already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    GOOGLE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Google user not found");

    private final HttpStatus httpStatus;
    private final String message;
}
