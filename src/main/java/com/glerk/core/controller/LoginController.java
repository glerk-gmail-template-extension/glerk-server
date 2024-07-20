package com.glerk.core.controller;

import com.glerk.core.dto.IdTokenRequestDto;
import com.glerk.core.service.UserService;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")
public class LoginController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signupWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody, HttpServletResponse response) {
        String authToken = userService.signupOAuthGoogle(requestBody);
        return handleGoogleOauth2(response, authToken);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody, HttpServletResponse response) {
        String authToken = userService.loginOAuthGoogle(requestBody);
        return handleGoogleOauth2(response, authToken);
    }

    private ResponseEntity<Void> handleGoogleOauth2(HttpServletResponse response, String authToken) {
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                .httpOnly(true)
                .maxAge(7 * 24 * 3600)
                .path("/")
                .secure(false)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}
