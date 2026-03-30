package com.mega.greentrade.manager;

import lombok.Data;

@Data
public class ManagerDTO {
    private int userno;
    private String user_name;
    private String user_call;
    private String user_id;
    private String user_password;
    private String email;
    private String nickname;
    private String imgurl;
    private int greenscore;
    private int sellcount;
    private String staff;
    private String address1;
    private String address2;
    private String postnum;
    private int targetid_count;
}
