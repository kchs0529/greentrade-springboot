package com.mega.greentrade.log;

import java.sql.Date;

public interface BuyLogProjection {
    String getTradestatus();
    String getTitle();
    String getPrice();
    Date getTradesuccessdate();
    String getPaymethod();
    String getTrademethod();
    int getProductno();
    int getBuyuserno();
}
