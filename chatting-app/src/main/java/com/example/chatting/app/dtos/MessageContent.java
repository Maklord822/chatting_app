package com.example.chatting.app.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageContent(@NotBlank(message = "content is required")
                             @Size(max = 500, message = "The length of the content should be between 1 and 500 characters")
                             String content
) {
}
