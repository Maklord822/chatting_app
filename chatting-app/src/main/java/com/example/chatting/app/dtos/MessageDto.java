package com.example.chatting.app.dtos;

import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record MessageDto(

        Long id,
        UserDto sender,
        @NotBlank(message = "content is required")
        @Size(min = 1, max = 500, message = "The length of the content should be between 1 and 500 characters")
        String content,
        Instant sentAt
       // ChatDto chat
) {
}
