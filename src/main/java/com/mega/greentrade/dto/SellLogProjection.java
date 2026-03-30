package com.mega.greentrade.dto;

import java.sql.Date;

public interface SellLogProjection {
    String getTradestatus();
    String getTitle();
    String getPrice();
    Date getTradestartdate();
    String getPaymethod();
    String getTrademethod();
    int getProductno();
    Integer getBuyuserno();
}
