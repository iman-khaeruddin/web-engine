package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class OrderRequestModel {
    private int id;

    private Warehouse warehouse;
    private WarehouseOwner warehouse_owner;
    private Users buyer;

    private String pic_name;
    private String pic_position;
    private String pic_email;
    private String pic_phone;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date start_date;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date end_date;

    private String warehouse_type;
    private String warehouse_service_type;
    private BigDecimal min_offer_price;
    private BigDecimal max_offer_price;
    private int storage_quantity;
    private MsUnit unit;
    private String storage_methode;
    private String kode_booking;
    private String reason_rejected;
    private int status;
    private Integer seller_price;
    private Integer mospaze_price;

    private FulfillmentSpace fulfillment_space;
    private LeasingSpace leasing_space;

    @Override
    public String toString() {
        return "OrderRequestModel{" +
                "id=" + id +
                ", warehouse=" + warehouse +
                ", warehouse_owner=" + warehouse_owner +
                ", buyer=" + buyer +
                ", pic_name='" + pic_name + '\'' +
                ", pic_position='" + pic_position + '\'' +
                ", pic_email='" + pic_email + '\'' +
                ", pic_phone='" + pic_phone + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", warehouse_type='" + warehouse_type + '\'' +
                ", warehouse_service_type='" + warehouse_service_type + '\'' +
                ", min_offer_price=" + min_offer_price +
                ", max_offer_price=" + max_offer_price +
                ", storage_quantity=" + storage_quantity +
                ", unit=" + unit +
                ", storage_methode='" + storage_methode + '\'' +
                ", kode_booking='" + kode_booking + '\'' +
                ", reason_rejected='" + reason_rejected + '\'' +
                ", status=" + status +
                ", seller_price=" + seller_price +
                ", mospaze_price=" + mospaze_price +
                ", fulfillment_space=" + fulfillment_space +
                ", leasing_space=" + leasing_space +
                '}';
    }
}
