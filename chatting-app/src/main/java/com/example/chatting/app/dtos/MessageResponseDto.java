package com.example.chatting.app.dtos;

public record MessageResponseDto(boolean sent, String response, MessageDto message) {
}
