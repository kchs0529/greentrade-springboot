package com.mega.greentrade.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomListDTO {
    private int chatroomId;
    private String productTitle;
    private String productImage;
    private String otherNickname;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private String role; // "구매자" or "판매자"
}
