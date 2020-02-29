package com.mospaze.werehouse.entity.projector;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WarehouseByStatus {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private String owner;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "status_verification")
    private int status_verification;

    @Column(name = "status_verification_desc")
    private String status_verification_desc;

    @Column(name = "status")
    private int status;

    @Column(name = "status_desc")
    private String status_desc;

    @Column(name = "visit_count")
    private int visit_count;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @Column(name = "is_partner")
    private int is_partner;

    @Column(name = "is_first_class_warehouse")
    private int is_first_class_warehouse;
}
