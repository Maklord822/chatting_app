package com.example.chatting.app.repositories;

import com.example.chatting.app.entities.Chat;
import com.example.chatting.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    boolean existsByIdAndUsersIncluded_Id(Long chatId, Long userId);

}
