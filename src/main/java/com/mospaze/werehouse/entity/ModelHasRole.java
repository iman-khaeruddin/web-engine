package com.mospaze.werehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "model_has_roles")
public class ModelHasRole implements Serializable {

    @Id
    @Column(name = "model_id")
    private int model_id;

    @Column(name = "role_id")
    private int role_id;

    @Column(name = "super_user")
    private Integer super_user;

    @Column(name = "model_type")
    private String model_type;
}
