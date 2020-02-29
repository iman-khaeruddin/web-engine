package com.mospaze.werehouse.entity.projector;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mospaze.werehouse.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
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

    @Column(name = "warehouse_id")
    private int warehouse_id;

    @Column(name = "warehouse_name")
    private String warehouse_name;

    @Column(name = "pic_name")
    private String pic_name;

    @Column(name = "status_desc")
    private String status_desc;

    @Column(name = "status")
    private int status;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date created_at;
}
