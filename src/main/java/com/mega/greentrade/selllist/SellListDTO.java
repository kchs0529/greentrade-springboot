package com.mega.greentrade.selllist;

import lombok.Data;
import java.sql.Date;

@Data
public class SellListDTO {
    private int selllistno;
    private Date selllistdate;
    private int productno;
    private int buyuserno;
    private int selluserno;
    private String title;
    private String image;
    private String price;
    private String paymethod;
    private String user_name;
}
