package com.mega.greentrade.dto;

import java.sql.Date;

public interface LikeListProjection {
    int getLikeno();
    Date getLikedate();
    int getUserno();
    int getLikestat();
    int getProductno();
    String getTitle();
    String getPrice();
    String getImage();
    String getUser_name();
    String getProductstatus();
}
