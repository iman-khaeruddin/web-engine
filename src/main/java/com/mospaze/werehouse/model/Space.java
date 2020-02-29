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
public class Space implements Serializable {
    private String space_name;
    private String space_value;

    @Override
    public String toString() {
        return "Space{" +
                "space_name='" + space_name + '\'' +
                ", space_value='" + space_value + '\'' +
                '}';
    }
}
