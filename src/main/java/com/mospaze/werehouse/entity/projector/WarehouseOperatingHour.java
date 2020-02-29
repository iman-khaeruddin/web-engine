package com.mospaze.werehouse.entity.projector;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 */
public interface WarehouseOperatingHour {
    int getId();
    String getOperating_hour_name();
    String getOpen();
    String getClose();
    Integer getNumber_of_shift();
    String getRegular_overtime();
    String getHoliday_overtime();
}
