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
 * @see Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_on_hand_new")
public class StockOnHandNew implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "warehouse")
    private int warehouse;

    @Column(name = "primary_key")
    private String primary_key;

    @Column(name = "pallet_num")
    private String pallet_num;

    @Column(name = "location")
    private String location;

    @Column(name = "sku")
    private String sku;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "record_num")
    private String record_num;

    @Column(name = "receive_date")
    private Date receive_date;

    @Column(name = "expire_date")
    private Date expire_date;

    @Column(name = "date")
    private Date date;

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
        return "StockOnHandNew{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", warehouse=" + warehouse +
                ", primary_key='" + primary_key + '\'' +
                ", pallet_num='" + pallet_num + '\'' +
                ", location='" + location + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", record_num='" + record_num + '\'' +
                ", receive_date=" + receive_date +
                ", expire_date=" + expire_date +
                ", date=" + date +
                ", batch='" + batch + '\'' +
                ", qty_release=" + qty_release +
                ", qty_outbound=" + qty_outbound +
                ", qty_stock=" + qty_stock +
                ", status_stock='" + status_stock + '\'' +
                ", pallet='" + pallet + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", updated_by=" + updated_by +
                ", updated_at=" + updated_at +
                '}';
    }
}
