package com.glerk.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUpdateDto {

    @NotBlank(message = "Group name is required")
    @Size(min = 1, max = 30, message = "Group name must be between 1 and 30 characters")
    private String name;
}
