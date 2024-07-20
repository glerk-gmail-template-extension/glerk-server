package com.glerk.core.dto;

import com.glerk.core.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String username;
    private String profileUrl;

    public UserDto(String email, String username, String profileUrl) {
        this.email = email;
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public static UserDto convertToDto(User user) {
        return new UserDto(user.getEmail(), user.getUsername(), user.getProfileUrl());
    }
}
