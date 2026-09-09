package com.example.chatting.app.repositories;

import com.example.chatting.app.entities.Message;
import org.hibernate.boot.jaxb.mapping.spi.JaxbPersistentAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message>  findByChatIdOrderBySentAtDesc(
            Long chatId,
            Pageable pageable
    );

}
