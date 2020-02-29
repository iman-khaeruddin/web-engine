package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.*;
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
public class WarehouseSizeAndFacilities {
    @NotNull
    private Double max_storage_height;

    @NotNull
    private Double max_storage_weight;

    @NotNull
    private Integer min_rent_day;

    private Double space;
    private Double palletnum;

    @NotNull
    private Integer display_size;

    private List<WarehouseCategoryModel> warehouse_category;
    private List<WarehouseServiceModel> warehouse_service;
    private List<MsWarehouseFeature> warehouse_feature;
    private List<WarehouseOperatingHourModel> warehouse_operating_hour;

    @NotNull
    private MsWarehouseType warehouse_type;

    @NotNull
    private MsWarehouseAutomation warehouse_automation;

    @NotNull
    private MsWarehouseTemperature warehouse_temperature;

    private Double occupancy_data;
    private String additional_information;

    @Override
    public String toString() {
        return "WarehouseSizeAndFacilities{" +
                "max_storage_height=" + max_storage_height +
                ", max_storage_weight=" + max_storage_weight +
                ", min_rent_day=" + min_rent_day +
                ", space=" + space +
                ", palletnum=" + palletnum +
                ", display_size=" + display_size +
                ", warehouse_category=" + warehouse_category +
                ", warehouse_service=" + warehouse_service +
                ", warehouse_feature=" + warehouse_feature +
                ", warehouse_operating_hour=" + warehouse_operating_hour +
                ", warehouse_type=" + warehouse_type +
                ", warehouse_automation=" + warehouse_automation +
                ", warehouse_temperature=" + warehouse_temperature +
                ", occupancy_data=" + occupancy_data +
                ", additional_information='" + additional_information + '\'' +
                '}';
    }
}
