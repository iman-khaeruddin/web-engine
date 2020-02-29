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
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Occupancied implements Serializable {
    private String occupied_name;
    private String occupied_value;

    @Override
    public String toString() {
        return "Occupancied{" +
                "occupied_name='" + occupied_name + '\'' +
                ", occupied_value='" + occupied_value + '\'' +
                '}';
    }
}
