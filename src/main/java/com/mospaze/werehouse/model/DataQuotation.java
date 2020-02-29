package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.projector.WarehouseFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DataQuotation {
    private String date;
    private String booking_no;

    private String buyer_company_name;
    private String buyer_pic_name;
    private String buyer_pic_email;
    private String buyer_pic_phone;

    private String admin_name;
    private String admin_position;

    private String warehouse_id;
    private String warehouse_location_area;
    private String warehouse_service_type;
    private String warehouse_available_space;
    private String warehouse_slug;

    private String start_date;
    private String end_date;
    private String period_of_service;

    private String warehouse_storage_service;
    private String warehouse_capacity;
    private String warehouse_uom;
    private String warehouse_price;
    private String warehouse_remark;

    private List<HandlingService> handling_service;

    private List<AdditionalService> additional_service;

    private List<Image> images;

    private List<WarehouseFeature> warehouse_facilities;

    @Override
    public String toString() {
        return "DataQuotation{" +
                "date='" + date + '\'' +
                ", booking_no='" + booking_no + '\'' +
                ", buyer_company_name='" + buyer_company_name + '\'' +
                ", buyer_pic_name='" + buyer_pic_name + '\'' +
                ", buyer_pic_email='" + buyer_pic_email + '\'' +
                ", buyer_pic_phone='" + buyer_pic_phone + '\'' +
                ", admin_name='" + admin_name + '\'' +
                ", admin_position='" + admin_position + '\'' +
                ", warehouse_id='" + warehouse_id + '\'' +
                ", warehouse_location_area='" + warehouse_location_area + '\'' +
                ", warehouse_service_type='" + warehouse_service_type + '\'' +
                ", warehouse_available_space='" + warehouse_available_space + '\'' +
                ", warehouse_slug='" + warehouse_slug + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", period_of_service='" + period_of_service + '\'' +
                ", warehouse_storage_service='" + warehouse_storage_service + '\'' +
                ", warehouse_capacity='" + warehouse_capacity + '\'' +
                ", warehouse_uom='" + warehouse_uom + '\'' +
                ", warehouse_price='" + warehouse_price + '\'' +
                ", warehouse_remark='" + warehouse_remark + '\'' +
                ", handling_service=" + handling_service +
                ", additional_service=" + additional_service +
                ", images=" + images +
                ", warehouse_facilities=" + warehouse_facilities +
                '}';
    }
}
