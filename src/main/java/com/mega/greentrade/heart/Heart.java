package com.mega.greentrade.heart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "heart")
@Getter @Setter @NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "heartSeq")
    @SequenceGenerator(name = "heartSeq", sequenceName = "like_no_seq", allocationSize = 1)
    @Column(name = "likeno")
    private int likeno;

    @Column(name = "likedate")
    private Date likedate;

    @Column(name = "likestat")
    private int likestat;

    @Column(name = "userno")
    private int userno;

    @Column(name = "productno")
    private int productno;
}
