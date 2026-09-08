package com.example.chatting.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 30, message = "The length of the name should be between 1 and 30 characters")
        String name,

        @NotBlank(message = "Password is required")
        @Size(min = 1, max = 50, message = "The length of the name should be between 1 and 50 characters")
        String password
) {
}
