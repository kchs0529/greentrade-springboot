package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "product")
@Getter @Setter @NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productno")
    private int productno;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private String price;

    @Column(name = "likenum")
    private int likenum;

    @Column(name = "productstatus")
    private String productStatus;

    @Column(name = "productdetail")
    private String productDetail;

    @Column(name = "paymethod")
    private String paymethod;

    @Column(name = "trademethod")
    private String trademethod;

    @Column(name = "sellstatus")
    private String sellstatus;

    @Column(name = "adddate")
    private Date adddate;

    @Column(name = "editdate")
    private Date editdate;

    @Column(name = "viewcount")
    private int viewcount;

    @Column(name = "userno")
    private int userno;

    @Column(name = "image")
    private String image;

    @Column(name = "tradelocation")
    private String tradelocation;
}
