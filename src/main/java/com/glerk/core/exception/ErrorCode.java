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
    GROUP_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "This group name is already in use. \nPlease try a different one."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not found."),
    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "Template not found."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found. \nPlease sign up."),
    GOOGLE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Google user not found."),
    NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "Not authorized to perform this action");

    private final HttpStatus httpStatus;
    private final String message;
}
