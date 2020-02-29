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
 * @since 02/12/2019
 * @see Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_list_new")
public class TransactionListNew implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "warehouse")
    private int warehouse;

    @Column(name = "sku")
    private String sku;

    @Column(name = "job_no")
    private String job_no;

    @Column(name = "location")
    private String location;

    @Column(name = "batch")
    private String batch;

    @Column(name = "from_to")
    private String from_to;

    @Column(name = "qty")
    private int qty;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;

    @Column(name = "packing")
    private String packing;

    @Column(name = "transport_type")
    private String transport_type;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "close_date")
    private Date close_date;

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
        return "TransactionListNew{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", warehouse=" + warehouse +
                ", sku='" + sku + '\'' +
                ", job_no='" + job_no + '\'' +
                ", location='" + location + '\'' +
                ", batch='" + batch + '\'' +
                ", from_to='" + from_to + '\'' +
                ", qty=" + qty +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", packing='" + packing + '\'' +
                ", transport_type='" + transport_type + '\'' +
                ", create_date=" + create_date +
                ", close_date=" + close_date +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", updated_by=" + updated_by +
                ", updated_at=" + updated_at +
                '}';
    }
}
