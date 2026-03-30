package com.mega.greentrade.product;

import java.sql.Date;

public interface ProductWithUserProjection {
    int getProductno();
    String getTitle();
    String getPrice();
    String getImage();
    String getUser_name();
    String getProductstatus();
    String getTrademethod();
    Date getAdddate();
    String getPaymethod();
    String getSellstatus();
    int getUserno();
    String getProductdetail();
    String getTradelocation();
    int getViewcount();
    int getLikenum();
}
