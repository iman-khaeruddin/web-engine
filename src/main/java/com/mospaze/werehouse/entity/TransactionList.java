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
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_list")
public class TransactionList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "job_no")
    private String job_no;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "location")
    private String location;

    @Column(name = "batch")
    private String batch;

    @Column(name = "from_to")
    private String from_to;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "packing_qty")
    private Integer packing_qty;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "customer")
    private String customer;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "category")
    private String category;

    @Column(name = "packing")
    private String packing;

    @Column(name = "transport_type")
    private String transport_type;

    @Column(name = "close_date")
    private Date close_date;

    @Override
    public String toString() {
        return "TransactionList{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", job_no='" + job_no + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", location='" + location + '\'' +
                ", batch='" + batch + '\'' +
                ", from_to='" + from_to + '\'' +
                ", qty=" + qty +
                ", packing_qty=" + packing_qty +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", create_date=" + create_date +
                ", customer='" + customer + '\'' +
                ", product_name='" + product_name + '\'' +
                ", category='" + category + '\'' +
                ", packing='" + packing + '\'' +
                ", transport_type='" + transport_type + '\'' +
                ", close_date=" + close_date +
                '}';
    }
}
