package com.mega.greentrade.selllist;

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
