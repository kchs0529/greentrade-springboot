package com.mega.greentrade.faq;

import lombok.Data;
import java.sql.Date;

@Data
public class FaqDTO {
    private int faqno;
    private String faqtitle;
    private String faqcontent;
    private Date faqdate;
    private String faqcate;
}
