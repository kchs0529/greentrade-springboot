package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "review")
@Getter @Setter @NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewno")
    private int reviewno;

    @Column(name = "reviewcontent")
    private String reviewcontent;

    @Column(name = "reviewscore")
    private int reviewscore;

    @Column(name = "reviewdate")
    private Date reviewdate;

    @Column(name = "reviewuserno")
    private int reviewuserno;

    @Column(name = "selleruserno")
    private int selleruserno;

    @Column(name = "sellername")
    private String sellername;

    @Column(name = "paymethod")
    private String paymethod;
}
