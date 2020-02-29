package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WarehouseLocationAndName {
    @NotNull
    private int id;

    @NotBlank
    @NotNull
    private String warehouse_name;

    @NotNull
    private WarehouseOwner warehouse_owner;

    @NotBlank
    @NotNull
    private String map_location;

    @NotBlank
    @NotNull
    private String map_lat;

    @NotBlank
    @NotNull
    private String map_lng;

    @NotNull
    @Min(1)
    private Integer district_id;

    private String district_name;

    @NotNull
    @Min(1)
    private Integer city_id;

    private String city_name;

    @NotNull
    @Min(1)
    private Integer state_id;

    private String state_name;

    @NotNull
    @Min(1)
    private Integer country_id;

    private String country_name;

    @NotBlank
    @NotNull
    private String postalcode;

    @NotBlank
    @NotNull
    private String address;

    @Override
    public String toString() {
        return "WarehouseLocationAndName{" +
                "warehouse_name='" + warehouse_name + '\'' +
                ", warehouse_owner=" + warehouse_owner +
                ", map_location='" + map_location + '\'' +
                ", map_lat='" + map_lat + '\'' +
                ", map_lng='" + map_lng + '\'' +
                ", city_id=" + city_id +
                ", state_id=" + state_id +
                ", country_id=" + country_id +
                ", postalcode='" + postalcode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
