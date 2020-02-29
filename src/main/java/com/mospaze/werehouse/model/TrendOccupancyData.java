package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TrendOccupancyData implements Serializable {
    private String date;
    private String warehouse_name;
    private Integer total_space;
    private Integer occupied;
    private Double occupancy;
    private Integer space;

    @Override
    public String toString() {
        return "TrendOccupancyData{" +
                "date=" + date +
                ", warehouse_name='" + warehouse_name + '\'' +
                ", total_space=" + total_space +
                ", occupied=" + occupied +
                ", occupancy=" + occupancy +
                ", space=" + space +
                '}';
    }
}
