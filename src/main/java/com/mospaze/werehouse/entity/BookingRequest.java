package com.mospaze.werehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "booking_request")
public class BookingRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "kode_booking")
    private String kode_booking;

    @Column(name = "quotation_no")
    private String quotation_no;

    @Column(name = "quotation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Jakarta")
    private Date quotation_date;

    @Column(name = "admin_id")
    private Integer admin_id;

    @Column(name = "buyer_id")
    private int buyer_id;

    @Column(name = "warehouse_id")
    private int warehouse_id;

    @Column(name = "pic_name")
    private String pic_name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "company_name")
    private String company_name;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Jakarta")
    private Date start_date;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Asia/Jakarta")
    private Date end_date;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "unit")
    private int unit;

    @Column(name = "category")
    private String category;

    @Column(name = "status")
    private int status;

    @Column(name = "reason_rejected")
    private String reason_rejected;

    @Column(name = "additional_info")
    private String additional_info;

    @Column(name = "seller_price")
    private Integer seller_price;

    @Column(name = "mospaze_price")
    private Integer mospaze_price;

    @Column(name = "is_leasing_space")
    private int is_leasing_space;

    @Column(name = "is_fulfillment_space")
    private int is_fulfillment_space;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date created_at;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date updated_at;

    @Override
    public String toString() {
        return "BookingRequest{" +
                "id=" + id +
                ", kode_booking='" + kode_booking + '\'' +
                ", quotation_no='" + quotation_no + '\'' +
                ", admin_id=" + admin_id +
                ", buyer_id=" + buyer_id +
                ", warehouse_id=" + warehouse_id +
                ", pic_name='" + pic_name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", company_name='" + company_name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", capacity=" + capacity +
                ", unit=" + unit +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", reason_rejected='" + reason_rejected + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", seller_price=" + seller_price +
                ", mospaze_price=" + mospaze_price +
                ", is_leasing_space=" + is_leasing_space +
                ", is_fulfillment_space=" + is_fulfillment_space +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
