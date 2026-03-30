package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "report")
@Getter @Setter @NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    private int reportid;

    @Column(name = "reporterid")
    private int reporterid;

    @Column(name = "targetid")
    private int targetid;

    @Column(name = "reportdate")
    private Date reportdate;

    @Column(name = "reportimgurl")
    private String reportimgurl;

    @Column(name = "reportcontent")
    private String reportcontent;

    @Column(name = "reporttitle")
    private String reporttitle;
}
