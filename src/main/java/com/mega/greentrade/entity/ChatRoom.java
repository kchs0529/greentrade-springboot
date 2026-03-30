package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chatroom")
@Getter @Setter @NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom")
    private int chatroomId;

    @Column(name = "buyer")
    private int buyer;

    @Column(name = "sellproduct")
    private int sellproduct;
}
