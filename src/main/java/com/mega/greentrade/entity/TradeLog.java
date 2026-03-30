package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "logs")
@Getter @Setter @NoArgsConstructor
public class TradeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tradeno")
    private int tradeno;

    @Column(name = "tradestartdate")
    private Date tradestartdate;

    @Column(name = "tradesuccessdate")
    private Date tradesuccessdate;

    @Column(name = "tradetype")
    private String tradetype;

    @Column(name = "trademethod")
    private String trademethod;

    @Column(name = "tradestatus")
    private String tradestatus;

    @Column(name = "productno")
    private int productno;

    @Column(name = "buyuserno")
    private Integer buyuserno;

    @Column(name = "selluserno")
    private int selluserno;
}
