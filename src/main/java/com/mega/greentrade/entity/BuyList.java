package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "buylist")
@Getter @Setter @NoArgsConstructor
public class BuyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buylistno")
    private int buylistno;

    @Column(name = "buylistdate")
    private Date buylistdate;

    @Column(name = "isreview")
    private String isreview;

    @Column(name = "productno")
    private int productno;

    @Column(name = "buyuserno")
    private int buyuserno;

    @Column(name = "selluserno")
    private int selluserno;
}
