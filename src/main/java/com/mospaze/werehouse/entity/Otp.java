package com.mospaze.werehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class Otp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "otp")
    private String otp;

    @Column(name = "status")
    private byte status;

    @Column(name = "attempt")
    private Integer attempt;

    @Column(name = "created_at")
    private Date created_at;
}
