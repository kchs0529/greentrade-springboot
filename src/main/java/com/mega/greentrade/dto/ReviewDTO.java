package com.mega.greentrade.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class ReviewDTO {
    private int reviewno;
    private String reviewcontent;
    private int reviewscore;
    private Date reviewdate;
    private int reviewuserno;
    private int selleruserno;
    private String sellername;
    private String paymethod;
    private int content_count;
}
