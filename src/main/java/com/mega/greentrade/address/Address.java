package com.mega.greentrade.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter @Setter @NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addressSeq")
    @SequenceGenerator(name = "addressSeq", sequenceName = "add_no_seq", allocationSize = 1)
    @Column(name = "addno")
    private int addno;

    @Column(name = "receivername")
    private String receivername;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "postnum")
    private String postnum;

    @Column(name = "userid")
    private String userid;
}
