package com.mega.greentrade.product;

import lombok.Data;
import java.sql.Date;

@Data
public class ProductDTO {
    private int productno;
    private String title;
    private String price;
    private int likenum;
    private String productStatus;
    private String productDetail;
    private String paymethod;
    private String trademethod;
    private String sellstatus;
    private Date adddate;
    private Date editdate;
    private int viewcount;
    private int userno;
    private String image;
    private String tradelocation;
    private String user_name;
}
