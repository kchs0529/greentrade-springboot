package com.mega.greentrade.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    Optional<ChatRoom> findByBuyerAndSellproduct(int buyer, int sellproduct);
}
