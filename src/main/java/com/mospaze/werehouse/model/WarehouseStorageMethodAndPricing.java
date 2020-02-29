package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.MsUnit;
import com.mospaze.werehouse.entity.MsWarehouseSpace;
import com.mospaze.werehouse.entity.MsWarehouseSpaceService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WarehouseStorageMethodAndPricing {
    @NotBlank
    @NotNull
    private String currency;

    @NotNull
    private MsUnit unit;

    private Integer has_rack_available;
    private Integer total_rack_available;
    private Double rack_rent_price;
    private Integer has_stack_available;
    private Integer total_stack_available;
    private Double stack_rent_price;
    
    @NotEmpty
    private List<MsWarehouseSpace> warehouse_space;

    private List<MsWarehouseSpaceService> warehouse_space_service;

    @Override
    public String toString() {
        return "WarehouseStorageMethodAndPricing{" +
                "currency='" + currency + '\'' +
                ", unit=" + unit +
                ", has_rack_available=" + has_rack_available +
                ", total_rack_available=" + total_rack_available +
                ", rack_rent_price=" + rack_rent_price +
                ", has_stack_available=" + has_stack_available +
                ", total_stack_available=" + total_stack_available +
                ", stack_rent_price=" + stack_rent_price +
                ", warehouse_space=" + warehouse_space +
                ", warehouse_space_service=" + warehouse_space_service +
                '}';
    }
}
