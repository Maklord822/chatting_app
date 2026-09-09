package com.example.chatting.app.dtos;

import com.example.chatting.app.entities.Message;

import java.util.List;

public record ChatDto(
        Long id,
        String name,
        List<MessageDto> messages,
        List<UserDto> includedUsers,
        UserDto creator
) {
}
