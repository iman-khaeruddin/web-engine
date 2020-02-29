package com.mospaze.werehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
@Table(name = "warehouse_operating_hour")
public class WarehouseOperatingHour {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "operating_hour_id")
    private int operating_hour_id;

    @Column(name = "warehouse_id")
    private int warehouse_id;

    @Column(name = "open")
    private String open;

    @Column(name = "close")
    private String close;

    @Column(name = "number_of_shift")
    private Integer number_of_shift;

    @Column(name = "regular_overtime")
    private String regular_overtime;

    @Column(name = "holiday_overtime")
    private String holiday_overtime;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;
}
