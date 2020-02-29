package com.mospaze.werehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trend_occupancy")
public class TrendOccupancy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String date;

    @Column(name = "occupied_c3b")
    private String occupied_c3b;

    @Column(name = "occupied_b2p")
    private String occupied_b2p;

    @Column(name = "occupied_b1l")
    private String occupied_b1l;

    @Column(name = "total_occupied")
    private String total_occupied;

    @Column(name = "occupancy_c3b")
    private String occupancy_c3b;

    @Column(name = "occupancy_b2p")
    private String occupancy_b2p;

    @Column(name = "occupancy_b1l")
    private String occupancy_b1l;

    @Column(name = "total_occupancy")
    private String total_occupancy;

    @Column(name = "space_c3b")
    private String space_c3b;

    @Column(name = "space_b2p")
    private String space_b2p;

    @Column(name = "space_b1l")
    private String space_b1l;

    @Column(name = "month")
    private String month;

    @Override
    public String toString() {
        return "WarehouseName{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", occupied_c3b='" + occupied_c3b + '\'' +
                ", occupied_b2p='" + occupied_b2p + '\'' +
                ", occupied_b1l='" + occupied_b1l + '\'' +
                ", total_occupied='" + total_occupied + '\'' +
                ", occupancy_c3b='" + occupancy_c3b + '\'' +
                ", occupancy_b2p='" + occupancy_b2p + '\'' +
                ", occupancy_b1l='" + occupancy_b1l + '\'' +
                ", total_occupancy='" + total_occupancy + '\'' +
                ", space_c3b='" + space_c3b + '\'' +
                ", space_b2p='" + space_b2p + '\'' +
                ", space_b1l='" + space_b1l + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
