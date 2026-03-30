package com.mega.greentrade.log;

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
