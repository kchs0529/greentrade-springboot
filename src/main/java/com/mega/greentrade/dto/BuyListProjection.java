package com.mega.greentrade.dto;

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
