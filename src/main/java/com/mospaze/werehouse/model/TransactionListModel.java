package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionListModel {

    private String warehouse;

    private String sku;

    private String job_no;

    private String location;

    private String batch;

    private String from_to;

    private String qty;

    private String type;

    private String status;

    private String category;

    private String packing;

    private String transport_type;

    private String create_date;

    private String close_date;

    @Override
    public String toString() {
        return "TransactionListModel{" +
                "warehouse='" + warehouse + '\'' +
                ", sku='" + sku + '\'' +
                ", job_no='" + job_no + '\'' +
                ", location='" + location + '\'' +
                ", batch='" + batch + '\'' +
                ", from_to='" + from_to + '\'' +
                ", qty='" + qty + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", packing='" + packing + '\'' +
                ", transport_type='" + transport_type + '\'' +
                ", create_date='" + create_date + '\'' +
                ", close_date='" + close_date + '\'' +
                '}';
    }
}
