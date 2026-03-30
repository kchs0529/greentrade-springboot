package com.mega.greentrade.heart;

import lombok.Data;
import java.sql.Date;

@Data
public class HeartDTO {
    private int likenum;
    private Date likedate;
    private int likestat;
    private int userno;
    private int productno;
}
