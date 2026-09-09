package com.example.chatting.app.services;

import com.example.chatting.app.dtos.ChatDto;
import com.example.chatting.app.dtos.MessageDto;
import com.example.chatting.app.dtos.UserDto;
import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.Message;
import com.example.chatting.app.entities.User;
import com.example.chatting.app.repositories.ChatRepository;
import com.example.chatting.app.repositories.MessageRepository;
import com.example.chatting.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatDto createChat(User user, String name) {

      Chat chat = new Chat();
      chat.setName(name);
      chat.setCreator(user);
      chat.setMessages(List.of());
      chat.setUsersIncluded(List.of(user));

      chatRepository.save(chat);

      return transformChatToDto(chat,0);

    }

    public String deleteChat(Long chatId, User user) {

        Chat chat = chatRepository.findById(chatId).orElseThrow();
        if (chat.getCreator()!=user) return "you don't have permission";

        chatRepository.delete(chat);

        return "deleted";
    }

    public String addUser(Long chatId, User adderUser, Long userId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow();

        System.out.println(adderUser);
        System.out.println(chat.getCreator());
        if (!Objects.equals(chat.getCreator().getId(), adderUser.getId())) return "you don't have permission";
        if (chatRepository.existsByIdAndUsersIncluded_Id(chatId, userId)) return "user is already added";
        if (!userRepository.existsById(userId)) return "user doesn't exist";

        List<User> usersIncluded = chat.getUsersIncluded();
        User addedUser = userRepository.findById(userId).orElseThrow();
        usersIncluded.add(addedUser);
        chat.setUsersIncluded(usersIncluded);

        chatRepository.save(chat);

        List<Chat> chatsOfAddedUser = addedUser.getChats();
        chatsOfAddedUser.add(chat);
        addedUser.setChats(chatsOfAddedUser);

        userRepository.save(addedUser);

        return "added";

    }

    public String removeUser(Long chatId, User removerUser, Long userId) {

        Chat chat = chatRepository.findById(chatId).orElseThrow();

        if (!Objects.equals(chat.getCreator().getId(), removerUser.getId())) return "you don't have permission";
        if (!chatRepository.existsByIdAndUsersIncluded_Id(chatId, userId)) return "user is already removed";
        if (!userRepository.existsById(userId)) return "user doesn't exist";

        List<User> usersIncluded = chat.getUsersIncluded();
        User removedUser = userRepository.findById(userId).orElseThrow();
        usersIncluded.remove(removedUser);
        chat.setUsersIncluded(usersIncluded);

        chatRepository.save(chat);

        List<Chat> chatsOfRemovedUser = removedUser.getChats();
        chatsOfRemovedUser.remove(chat);
        removedUser.setChats(chatsOfRemovedUser);

        userRepository.save(removedUser);

        return "removed";

    }

    public String leaveChat(Long chatId, User user) {

        Chat chat = chatRepository.findById(chatId).orElseThrow();
        if (!chatRepository.existsByIdAndUsersIncluded_Id(chatId, user.getId())) return "user already left";
        //if (chat.getCreator()==user) return "creator can't leave chat";

        List<User> usersIncluded = chat.getUsersIncluded();
        usersIncluded.remove(user);
        if (chat.getCreator()==user) {
            if (!usersIncluded.isEmpty()) {
                chat.setCreator(usersIncluded.getFirst());
            } else {
                chatRepository.delete(chat);
                return "left";
            }
        }
        chat.setUsersIncluded(usersIncluded);

        chatRepository.save(chat);

        List<Chat> chatsOfLeftUser = user.getChats();
        chatsOfLeftUser.remove(chat);
        user.setChats(chatsOfLeftUser);

        userRepository.save(user);

        return "left";
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
                Sort.by("sentAt").descending()
        );

        return messageRepository.findByChatId(chatId, pageable);

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
