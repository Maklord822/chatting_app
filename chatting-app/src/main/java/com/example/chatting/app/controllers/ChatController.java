package com.example.chatting.app.controllers;

import com.example.chatting.app.dtos.ChatDto;
import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.services.ChatService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<?> createChat(Authentication authentication,
                                        @RequestParam @Validated  @NotBlank(message = "Name is required")
                                        @Size(min = 1, max = 30, message = "The length of the name should be between 1 and 30 characters")
                                        String name) {
        User user = (User) authentication.getPrincipal();

        ChatDto chat = chatService.createChat(user, name);

        return ResponseEntity.ok(chat);
    }

    @GetMapping("/fetch/{chatId}")
    public ResponseEntity<ChatDto> fetchChat(Authentication authentication,
                                             @PathVariable(name = "chatId") Long id,
                                             @RequestParam(name = "page") int page) {

        User user = (User) authentication.getPrincipal();

       return ResponseEntity.ok(chatService.getChatById(id,user, page));
    }
}
