package com.example.chatting.app.dtos;

import com.example.chatting.app.entities.Chat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserDto(Long id,
                      String name
                     // String password,
                   //   List<ChatDto> chats
                         ) {
}
