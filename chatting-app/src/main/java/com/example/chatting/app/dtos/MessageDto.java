package com.example.chatting.app.dtos;

import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record MessageDto(

        Long id,
        UserDto sender,
        String content,
        Instant sentAt
       // ChatDto chat
) {
}
