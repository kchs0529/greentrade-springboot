package com.mega.greentrade.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private int senderUserno;
    private String senderNickname;
    private String message;
    private String sentAt;
}
