package com.glerk.core.controller;

import com.glerk.core.dto.IdTokenRequestDto;
import com.glerk.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signupWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody) {
        String authToken = userService.signupOAuthGoogle(requestBody.getIdToken());
        return ResponseEntity.ok().header("Authorization", "Bearer " + authToken).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody) {
        String authToken = userService.loginOAuthGoogle(requestBody.getIdToken());
        return ResponseEntity.ok().header("Authorization", "Bearer " + authToken).build();
    }
}
