package com.example.chatting.app.dtos;

public record UserResponseDto(boolean registered, String message, UserDto userDto) {
}
