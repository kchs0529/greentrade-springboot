package com.mega.greentrade.dto;

import java.sql.Date;

public interface SellListProjection {
    Date getSelllistdate();
    String getImage();
    int getProductno();
    String getTitle();
    String getPrice();
    String getPaymethod();
    String getBuyername();
}
