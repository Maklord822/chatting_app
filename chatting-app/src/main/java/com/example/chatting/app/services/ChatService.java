package com.example.chatting.app.services;

import com.example.chatting.app.dtos.ChatDto;
import com.example.chatting.app.dtos.MessageDto;
import com.example.chatting.app.dtos.UserDto;
import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.Message;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.ChatRepository;
import com.example.chatting.app.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;

    public ChatDto createChat(User user, String name) {

      Chat chat = new Chat();
      chat.setName(name);
      chat.setCreator(user);
      chat.setMessages(List.of());
      chat.setUsersIncluded(List.of(user));

      chatRepository.save(chat);

      return transformChatToDto(chat,0);

    }

    public ChatDto getChatById(Long id, User user, int messagesPage) {

        Chat chat = chatRepository.findById(id).orElseThrow();

        if (!chatRepository.existsByIdAndUsersIncluded_Id(id, user.getId()))  {
         return null;
        }

        return transformChatToDto(chat, messagesPage);

    }

    public Page<Message> fetchMessages(Long chatId, int page) {

        Pageable pageable = PageRequest.of(
                page,
                20,
                Sort.by("SentAt").descending()
        );

        return messageRepository.findByChatIdOrderBySentAtDesc(chatId, pageable);

    }

    public ChatDto transformChatToDto(Chat chat, int messagesPage) {

        List<User> includedUsers = chat.getUsersIncluded();

        List<UserDto> includedUsersDto = includedUsers.stream()
                .map(userService::transformUserToDto)
                .toList();

        return new ChatDto(
                chat.getId(),
                chat.getName(),
                fetchMessages(chat.getId(),messagesPage).stream().map(this::transformMessageToDto).toList(),
                includedUsersDto,
                userService.transformUserToDto(chat.getCreator())
        );
    }

    public MessageDto transformMessageToDto(Message message) {

        return new MessageDto(
                message.getId(),
                userService.transformUserToDto(message.getSender()),
                message.getContent(),
                message.getSentAt()

        );

    }

}
