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
 * @since 17/12/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "master_warehouse_operating_hour")
public class MsWarehouseOperatingHour implements Serializable {

    private static final long serialVersionUID = -1081992063365087505L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "operation_hour_name")
    private String operation_hour_name;

    @Column(name = "status")
    private int status;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "update_at")
    private Date update_at;
}
