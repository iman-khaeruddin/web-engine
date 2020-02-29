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
public class DetailCities implements Serializable {

    private int id_cities;
    private String cities_name;
    private int id_state;
    private String state_name;
    private int id_countries;
    private String countries_name;
}
