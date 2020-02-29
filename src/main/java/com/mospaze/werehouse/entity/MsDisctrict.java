package com.mospaze.werehouse.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 28/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "master_location_districts")
public class MsDisctrict {

    private static final long serialVersionUID = -2725324516710104469L;

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "cities_id")
    private int cities_id;

    @Column(name = "name")
    private String name;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longtitude")
    private Double longtitude;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;
}
