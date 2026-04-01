package com.mega.greentrade.repository;

import com.mega.greentrade.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatroomIdOrderBySentAtAsc(int chatroomId);
    Optional<ChatMessage> findTopByChatroomIdOrderBySentAtDesc(int chatroomId);
}
