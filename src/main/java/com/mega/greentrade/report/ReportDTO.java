package com.mega.greentrade.report;

import lombok.Data;
import java.sql.Date;

@Data
public class ReportDTO {
    private int reportid;
    private int reporterid;
    private int targetid;
    private Date reportdate;
    private String reportimgurl;
    private String reportcontent;
    private String reporttitle;
    private String reporterNickname;
    private String targetNickname;
}
