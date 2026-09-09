package com.example.chatting.app.controllers;

import com.example.chatting.app.dtos.MessageDto;
import com.example.chatting.app.dtos.MessageResponseDto;
import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.entities.Message;
import com.example.chatting.app.repositories.ChatRepository;
import com.example.chatting.app.repositories.MessageRepository;
import com.example.chatting.app.repositories.UserRepository;
import com.example.chatting.app.services.ChatService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    @MessageMapping("/hello")
    @SendTo("/chat/{pathId}")
    public MessageResponseDto sendMessage(Authentication authentication,
                                          @PathVariable(name = "pathId") Long id,
                                          @RequestParam(name = "content")
                                                   @Validated
                                                   @NotBlank(message = "content is required")
                                                   @Size(min = 1, max = 500, message = "The length of the content should be between 1 and 500 characters")
                                               String content) throws Exception {
            User user = (User) authentication.getPrincipal();

        if (!chatRepository.existsByIdAndUsersIncluded_Id(id, user.getId())) return
                new MessageResponseDto(false, "user isn't included", null);

        Chat chat = chatRepository.findById(id).orElseThrow();

        Message message = new Message();
        message.setSender(user);
        message.setContent(content);
        message.setChat(chat);

        messageRepository.save(message);

        List<Message> messages = chat.getMessages();
        messages.add(message);
        chat.setMessages(messages);

        chatRepository.save(chat);

        MessageDto sendingMessageDto = chatService.transformMessageToDto(message);
        MessageResponseDto sendingMessage = new MessageResponseDto(true, "sent", sendingMessageDto);


            Thread.sleep(1000); // simulated delay
            return sendingMessage;
    }
}