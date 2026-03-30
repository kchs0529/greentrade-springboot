package com.mega.greentrade.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chatroom")
@Getter @Setter @NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatRoomSeq")
    @SequenceGenerator(name = "chatRoomSeq", sequenceName = "chat_room_seq", allocationSize = 1)
    @Column(name = "chatroom")
    private int chatroomId;

    @Column(name = "buyer")
    private int buyer;

    @Column(name = "sellproduct")
    private int sellproduct;
}
