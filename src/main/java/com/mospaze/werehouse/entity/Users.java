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
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "users")
public class Users  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "company")
    private String company;

    @Column(name = "company_url")
    private String company_url;

    @Column(name = "company_tax_number")
    private String company_tax_number;

    @Column(name = "job_tittle")
    private String job_tittle;

    @Column(name = "address_1")
    private String address_1;

    @Column(name = "address_2")
    private String address_2;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postalcode")
    private String postalcode;

    @Column(name = "country")
    private String country;

    @Column(name = "country_id")
    private Integer country_id;

    @Column(name = "state_id")
    private Integer state_id;

    @Column(name = "city_id")
    private Integer city_id;

    @Column(name = "district_id")
    private Integer district_id;

    @Column(name = "confirmation_code")
    private String confirmation_code;

    @Column(name = "confirmed")
    private int confirmed;

    @Column(name = "verified_phone")
    private int verified_phone;

    @Column(name = "status")
    private int status;

    @Column(name = "is_multi_user")
    private int is_multi_user;

    @Column(name = "is_primary_user")
    private int is_primary_user;

    @Column(name = "flag_change_password")
    private Date flag_change_password;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;
}
