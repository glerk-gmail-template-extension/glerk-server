package com.glerk.core.controller;

import com.glerk.core.dto.UserDto;
import com.glerk.core.entity.User;
import com.glerk.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.glerk.core.dto.UserDto.convertToDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(Principal principal) {
        User user = userService.findUser(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertToDto(user));
    }
}
