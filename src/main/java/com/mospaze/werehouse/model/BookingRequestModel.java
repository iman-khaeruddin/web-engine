package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mospaze.werehouse.entity.FulfillmentSpace;
import com.mospaze.werehouse.entity.LeasingSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BookingRequestModel {

    private int id;

    @NotBlank
    @NotNull
    private String kode_booking;

    @NotNull
    private Integer warehouse_id;

    private String name;

    private String email;

    private String phone_number;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date start_date;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date end_date;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private Integer unit;

    @NotEmpty
    private List<String> category;

    private FulfillmentSpace fulfillment_space;
    private LeasingSpace leasing_space;

    @Override
    public String toString() {
        return "BookingRequestModel{" +
                "id=" + id +
                ", kode_booking='" + kode_booking + '\'' +
                ", warehouse_id=" + warehouse_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", capacity=" + capacity +
                ", unit=" + unit +
                ", category=" + category +
                ", fulfillment_space=" + fulfillment_space +
                ", leasing_space=" + leasing_space +
                '}';
    }
}
