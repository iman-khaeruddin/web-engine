package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Occupancy implements Serializable {
    private String occupancy_name;
    private String occupancy_value;

    @Override
    public String toString() {
        return "Occupancy{" +
                "occupancy_name='" + occupancy_name + '\'' +
                ", occupancy_value='" + occupancy_value + '\'' +
                '}';
    }
}
