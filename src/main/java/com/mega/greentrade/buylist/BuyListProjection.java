package com.mega.greentrade.buylist;

import java.sql.Date;

public interface BuyListProjection {
    Date getBuylistdate();
    String getIsreview();
    String getImage();
    int getProductno();
    String getTitle();
    String getPrice();
    String getPaymethod();
    String getSellername();
}
