package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 06/12/2019
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrendOccupancyNewModel {

    private String warehouse;

    private String total_space;

    private String occupied;

    private String occupancy;

    private String space;

    private String date;

    @Override
    public String toString() {
        return "TrendOccupancyNewModel{" +
                "warehouse=" + warehouse +
                ", total_space=" + total_space +
                ", occupied=" + occupied +
                ", occupancy=" + occupancy +
                ", space=" + space +
                ", date=" + date +
                '}';
    }
}
