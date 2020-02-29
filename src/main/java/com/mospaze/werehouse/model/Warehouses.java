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
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Warehouses implements Serializable {
    private String name;
    private String capacity;

    @Override
    public String toString() {
        return "warehouse{" +
                "name='" + name + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
