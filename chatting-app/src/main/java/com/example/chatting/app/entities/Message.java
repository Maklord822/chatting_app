package com.example.chatting.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JoinColumn(name = "sender")
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private User sender;

    @Column(name = "content")
    @Size(max = 500, min = 1)
    private String content;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private Instant SentAt;

    @JoinColumn(name = "chat")
    @ManyToOne(fetch = FetchType.EAGER)
    private Chat chat;
}
