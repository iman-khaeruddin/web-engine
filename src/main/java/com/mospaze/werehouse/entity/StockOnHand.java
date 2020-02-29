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
@Table(name = "stock_on_hand")
public class StockOnHand implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private String date;

    @Column(name = "primary_key")
    private String primary_key;

    @Column(name = "gedung")
    private String gedung;

    @Column(name = "pallet_num")
    private String pallet_num;

    @Column(name = "location")
    private String lokasi;

    @Column(name = "sku")
    private String sku;

    @Column(name = "description")
    private String description;

    @Column(name = "record_num")
    private String record_num;

    @Column(name = "receive_date")
    private String receive_date;

    @Column(name = "expire_date")
    private String expire_date;

    @Column(name = "batch")
    private String batch;

    @Column(name = "qty_release")
    private int qty_release;

    @Column(name = "qty_outbound")
    private int qty_outbound;

    @Column(name = "qty_stock")
    private int qty_stock;

    @Column(name = "status_stock")
    private String status_stock;

    @Column(name = "pallet")
    private String pallet;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "month")
    private String month;

    @Override
    public String toString() {
        return "StockOnHand{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", primary_key='" + primary_key + '\'' +
                ", gedung='" + gedung + '\'' +
                ", pallet_num='" + pallet_num + '\'' +
                ", location='" + lokasi + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", record_num='" + record_num + '\'' +
                ", receive_date='" + receive_date + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", batch='" + batch + '\'' +
                ", qty_release=" + qty_release +
                ", qty_outbound=" + qty_outbound +
                ", qty_stock=" + qty_stock +
                ", status_stock='" + status_stock + '\'' +
                ", pallet='" + pallet + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
