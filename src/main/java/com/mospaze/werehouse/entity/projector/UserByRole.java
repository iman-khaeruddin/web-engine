package com.mospaze.werehouse.entity.projector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserByRole {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "user_type")
    private String user_type;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private int status;

    @Column(name = "verified_phone")
    private int verified_phone;
}
