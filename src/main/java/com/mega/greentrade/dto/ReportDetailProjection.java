package com.mega.greentrade.dto;

import java.sql.Date;

public interface ReportDetailProjection {
    int getReportid();
    int getReporterid();
    int getTargetid();
    Date getReportdate();
    String getReportimgurl();
    String getReportcontent();
    String getReporttitle();
    String getReporterNickname();
    String getTargetNickname();
}
