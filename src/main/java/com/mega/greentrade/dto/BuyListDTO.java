package com.mega.greentrade.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class BuyListDTO {
    private int buylistno;
    private Date buylistdate;
    private String isreview;
    private int productno;
    private int buyuserno;
    private int selluserno;
    private String title;
    private String image;
    private String price;
    private String paymethod;
    private String user_name;
}
