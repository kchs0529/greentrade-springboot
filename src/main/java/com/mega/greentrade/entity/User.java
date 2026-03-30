package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_table_real")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userno")
    private int userno;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "user_call")
    private String user_call;

    @Column(name = "user_id", unique = true)
    private String user_id;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "imgurl")
    private String imgurl;

    @Column(name = "greenscore")
    private int greenscore;

    @Column(name = "sellcount")
    private int sellcount;

    @Column(name = "staff")
    private String staff;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "postnum")
    private String postnum;
}
