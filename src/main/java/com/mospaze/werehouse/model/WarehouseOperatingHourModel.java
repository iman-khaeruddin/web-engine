package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 * @see java.io.Serializable
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseOperatingHourModel {
    private int id;
    private int status;
    private String operation_hour_name;
    private String open;
    private String close;
    private Integer number_of_shift;
    private String regular_overtime;
    private String holiday_overtime;
}
