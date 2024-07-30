package com.glerk.core.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class AutocompleteEmailDto {

    private String email;
    private LocalDateTime updatedAt;

    public AutocompleteEmailDto(String email, LocalDateTime updatedAt) {
        this.email = email;
        this.updatedAt = updatedAt;
    }
}
