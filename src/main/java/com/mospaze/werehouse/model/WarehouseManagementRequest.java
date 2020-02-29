package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseManagementRequest {
    @NotNull
    private WarehouseLocationAndName warehouse_location_and_name;

    @NotNull
    private WarehouseSizeAndFacilities warehouse_size_and_facilities;

    @NotNull
    private WarehouseStorageMethodAndPricing warehouse_storage_and_pricing;

    private List<Media> media;

    private Integer main_image;

    private Integer is_partner;

    private Integer is_first_class_warehouse;

    @NotNull
    private Integer status_available;

    private String unavailable_text;

    private Integer status;
    private Integer status_verification;

    @Override
    public String toString() {
        return "WarehouseManagementRequest{" +
                "warehouse_location_and_name=" + warehouse_location_and_name +
                ", warehouse_size_and_facilities=" + warehouse_size_and_facilities +
                ", warehouse_storage_and_pricing=" + warehouse_storage_and_pricing +
                ", media=" + media +
                ", main_image=" + main_image +
                ", status_available=" + status_available +
                ", unavailable_text='" + unavailable_text + '\'' +
                ", status=" + status +
                ", status_verification=" + status_verification +
                '}';
    }
}
