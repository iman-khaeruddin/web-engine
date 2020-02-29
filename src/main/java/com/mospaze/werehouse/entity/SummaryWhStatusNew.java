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
 * @since 07/12/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "summary_wh_status_new")
public class SummaryWhStatusNew implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "warehouse")
    private int warehouse;

    @Column(name = "inbound_in_plt")
    private int inbound_in_plt;

    @Column(name = "outbound_in_plt")
    private int outbound_in_plt;

    @Column(name = "throughput_in_plt")
    private int throughput_in_plt;

    @Column(name = "inbound_in_case")
    private int inbound_in_case;

    @Column(name = "outbound_in_case")
    private int outbound_in_case;

    @Column(name = "throughput_in_case")
    private int throughput_in_case;

    @Column(name = "occupancy")
    private double occupancy;

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
        return "SummaryWhStatusNew{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", warehouse=" + warehouse +
                ", inbound_in_plt=" + inbound_in_plt +
                ", outbound_in_plt=" + outbound_in_plt +
                ", throughput_in_plt=" + throughput_in_plt +
                ", inbound_in_case=" + inbound_in_case +
                ", outbound_in_case=" + outbound_in_case +
                ", throughput_in_case=" + throughput_in_case +
                ", occupancy=" + occupancy +
                ", date=" + date +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", updated_by=" + updated_by +
                ", updated_at=" + updated_at +
                '}';
    }
}
