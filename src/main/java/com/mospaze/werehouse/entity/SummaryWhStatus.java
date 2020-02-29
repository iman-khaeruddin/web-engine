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
@Table(name = "summary_wh_status")
public class SummaryWhStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String date;

    @Column(name = "inbound_c3b")
    private String inbound_c3b;

    @Column(name = "inbound_b2p")
    private String inbound_b2p;

    @Column(name = "inbound_b1l")
    private String inbound_b1l;

    @Column(name = "total_inbound")
    private String total_inbound;

    @Column(name = "total_inb_per")
    private String total_inb_per;

    @Column(name = "outbound_c3b")
    private String outbound_c3b;

    @Column(name = "outbound_b2p")
    private String outbound_b2p;

    @Column(name = "outbound_b1l")
    private String outbound_b1l;

    @Column(name = "total_outbound")
    private String total_outbound;

    @Column(name = "total_out_per")
    private String total_out_per;

    @Column(name = "total_inbound_c3b")
    private String total_inbound_c3b;

    @Column(name = "total_inbound_b2p")
    private String total_inbound_b2p;

    @Column(name = "total_inbound_b1l")
    private String total_inbound_b1l;

    @Column(name = "total_inbound_tot")
    private String total_inbound_tot;

    @Column(name = "total_outbound_c3b")
    private String total_outbound_c3b;

    @Column(name = "total_outbound_b2p")
    private String total_outbound_b2p;

    @Column(name = "total_outbound_b1l")
    private String total_outbound_b1l;

    @Column(name = "total_outbound_tot")
    private String total_outbound_tot;

    @Column(name = "occupied_c3b")
    private String occupied_c3b;

    @Column(name = "occupied_b2p")
    private String occupied_b2p;

    @Column(name = "occupied_b1l")
    private String occupied_b1l;

    @Column(name = "total_occupied")
    private String total_occupied;

    @Column(name = "total_in_pp")
    private String total_in_pp;

    @Column(name = "available_space")
    private String available_space;

    @Column(name = "month")
    private String month;

    @Override
    public String toString() {
        return "SummaryWhStatusService{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", inbound_c3b='" + inbound_c3b + '\'' +
                ", inbound_b2p='" + inbound_b2p + '\'' +
                ", inbound_b1l='" + inbound_b1l + '\'' +
                ", total_inbound='" + total_inbound + '\'' +
                ", total_inb_per='" + total_inb_per + '\'' +
                ", outbound_c3b='" + outbound_c3b + '\'' +
                ", outbound_b2p='" + outbound_b2p + '\'' +
                ", outbound_b1l='" + outbound_b1l + '\'' +
                ", total_outbound='" + total_outbound + '\'' +
                ", total_out_per='" + total_out_per + '\'' +
                ", total_inbound_c3b='" + total_inbound_c3b + '\'' +
                ", total_inbound_b2p='" + total_inbound_b2p + '\'' +
                ", total_inbound_b1l='" + total_inbound_b1l + '\'' +
                ", total_inbound_tot='" + total_inbound_tot + '\'' +
                ", total_outbound_c3b='" + total_outbound_c3b + '\'' +
                ", total_outbound_b2p='" + total_outbound_b2p + '\'' +
                ", total_outbound_b1l='" + total_outbound_b1l + '\'' +
                ", total_outbound_tot='" + total_outbound_tot + '\'' +
                ", occupied_c3b='" + occupied_c3b + '\'' +
                ", occupied_b2p='" + occupied_b2p + '\'' +
                ", occupied_b1l='" + occupied_b1l + '\'' +
                ", total_occupied='" + total_occupied + '\'' +
                ", total_in_pp='" + total_in_pp + '\'' +
                ", available_space='" + available_space + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
