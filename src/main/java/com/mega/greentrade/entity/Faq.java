package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "faq")
@Getter @Setter @NoArgsConstructor
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faqno")
    private int faqno;

    @Column(name = "faqtitle")
    private String faqtitle;

    @Column(name = "faqcontent")
    private String faqcontent;

    @Column(name = "faqdate")
    private Date faqdate;

    @Column(name = "faqcate")
    private String faqcate;
}
