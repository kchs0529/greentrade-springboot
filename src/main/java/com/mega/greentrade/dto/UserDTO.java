package com.mega.greentrade.dto;
import com.mega.greentrade.entity.User;

import lombok.Data;

@Data
public class UserDTO {
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
    private int reviewCount;

    public static UserDTO from(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserno(user.getUserno());
        dto.setUser_name(user.getUser_name());
        dto.setUser_call(user.getUser_call());
        dto.setUser_id(user.getUser_id());
        dto.setUser_password(user.getUser_password());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setImgurl(user.getImgurl());
        dto.setGreenscore(user.getGreenscore());
        dto.setSellcount(user.getSellcount());
        dto.setStaff(user.getStaff());
        dto.setAddress1(user.getAddress1());
        dto.setAddress2(user.getAddress2());
        dto.setPostnum(user.getPostnum());
        return dto;
    }
}
