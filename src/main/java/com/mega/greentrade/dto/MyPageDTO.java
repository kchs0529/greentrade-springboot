package com.mega.greentrade.dto;

import lombok.Data;

@Data
public class MyPageDTO {
    private int userno;
    private String user_id;
    private String imgurl;
    private String user_name;
    private String user_call;
    private String email;
    private String address1;
    private String address2;
    private int buylistcount;
    private int selllistcount;
    private int reviewcount;
}
