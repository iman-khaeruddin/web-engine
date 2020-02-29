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
public class StockOnHandNewModel {
    private String warehouse;

    private String primary_key;

    private String pallet_num;

    private String location;

    private String sku;

    private String description;

    private String category;

    private String record_num;

    private String receive_date;

    private String expire_date;

    private String batch;

    private String qty_release;

    private String qty_outbound;

    private String qty_stock;

    private String status_stock;

    private String pallet;

    private String keterangan;

    private String date;

    @Override
    public String toString() {
        return "StockOnHandNewModel{" +
                "warehouse='" + warehouse + '\'' +
                ", primary_key='" + primary_key + '\'' +
                ", pallet_num='" + pallet_num + '\'' +
                ", location='" + location + '\'' +
                ", sku='" + sku + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", record_num='" + record_num + '\'' +
                ", receive_date='" + receive_date + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", batch='" + batch + '\'' +
                ", qty_release='" + qty_release + '\'' +
                ", qty_outbound='" + qty_outbound + '\'' +
                ", qty_stock='" + qty_stock + '\'' +
                ", status_stock='" + status_stock + '\'' +
                ", pallet='" + pallet + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
