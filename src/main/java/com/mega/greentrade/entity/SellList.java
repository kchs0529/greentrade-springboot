package com.mega.greentrade.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "selllist")
@Getter @Setter @NoArgsConstructor
public class SellList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selllistno")
    private int selllistno;

    @Column(name = "selllistdate")
    private Date selllistdate;

    @Column(name = "productno")
    private int productno;

    @Column(name = "buyuserno")
    private int buyuserno;

    @Column(name = "selluserno")
    private int selluserno;
}
