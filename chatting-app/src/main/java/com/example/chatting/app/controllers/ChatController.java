package com.example.chatting.app.controllers;

import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.ChatRepository;
import com.example.chatting.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    @MessageMapping("/hello")
    @SendTo("/chat/{pathId}")
    public ResponseEntity<Message> sendMessage(Authentication authentication,
                               @PathVariable(name = "pathId") Long id,
                               @RequestBody Message message) throws Exception {
            User user = (User) authentication.getPrincipal();

        Chat chat = user.getChats().stream()
                .filter(c -> c.getId().equals(11L))
                .findFirst()
                .orElseThrow();



            Thread.sleep(1000); // simulated delay
            return null;
    }
}