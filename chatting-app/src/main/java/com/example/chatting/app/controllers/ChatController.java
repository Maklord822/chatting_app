package com.example.chatting.app.controllers;

import com.example.chatting.app.dtos.ChatDto;
import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.ChatRepository;
import com.example.chatting.app.repositories.UserRepository;
import com.example.chatting.app.services.ChatService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatRepository chatRepository;

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

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<?> deleteChat(Authentication authentication,
                                             @PathVariable(name = "chatId") Long id) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(chatService.deleteChat(id, user));
    }
    @PostMapping("/{chatId}/add/{userName}")
    public ResponseEntity<?> addUser(Authentication authentication,
                                     @PathVariable(name = "chatId") Long chatId,
                                     @PathVariable(name = "userName") String userName) {

        User user = (User) authentication.getPrincipal();
        System.out.println(user);

        return ResponseEntity.ok(chatService.addUser(chatId, user, userName));


    }

    @DeleteMapping("/{chatId}/remove/{userName}")
    public ResponseEntity<?> removeUser(Authentication authentication,
                                     @PathVariable(name = "chatId") Long chatId,
                                     @PathVariable(name = "userName") String userName) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(chatService.removeUser(chatId, user, userName));


    }

    @DeleteMapping("/leave/{chatId}")
    public ResponseEntity<?> leaveChat(Authentication authentication,
                                        @PathVariable(name = "chatId") Long chatId) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(chatService.leaveChat(chatId, user));

    }
    @GetMapping("/fetch/chats")
    public ResponseEntity<List<ChatDto>> fetchChatsOfUser(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<ChatDto> chatDtos = new ArrayList<>();
        System.out.println(user.getChats().size());
       for (Chat chat : user.getChats()) {

           ChatDto chatDto = chatService.transformChatToDto(chat,0);
           chatDtos.add(chatDto);
       }

        return ResponseEntity.ok(chatDtos);
    }
}
