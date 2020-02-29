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
 * @since 06/12/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trend_occupancy_new")
public class TrendOccupancyNew implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "warehouse")
    private int warehouse;

    @Column(name = "total_space")
    private int total_space;

    @Column(name = "occupied")
    private int occupied;

    @Column(name = "occupancy")
    private double occupancy;

    @Column(name = "space")
    private int space;

    @Column(name = "date")
    private Date date;

    @Column(name = "created_by")
    private Integer created_by;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_by")
    private Integer updated_by;

    @Column(name = "updated_at")
    private Date updated_at;

    @Override
    public String toString() {
        return "TrendOccupancyNew{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", warehouse=" + warehouse +
                ", total_space=" + total_space +
                ", occupied=" + occupied +
                ", occupancy=" + occupancy +
                ", space=" + space +
                ", date=" + date +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", updated_by=" + updated_by +
                ", updated_at=" + updated_at +
                '}';
    }
}
