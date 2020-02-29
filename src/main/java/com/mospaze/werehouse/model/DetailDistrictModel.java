package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.MsDisctrict;
import com.mospaze.werehouse.entity.projector.DetailDistrict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 08/12/2019
 * @see java.io.Serializable
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DetailDistrictModel implements Serializable {
    private int id_cities;

    private String cities_name;

    private int id_state;

    private String state_name;

    private int id_countries;

    private String countries_name;

    private List<DetailDistrict> detail_district;

    @Override
    public String toString() {
        return "DetailDistrictModel{" +
                "id_cities=" + id_cities +
                ", cities_name='" + cities_name + '\'' +
                ", id_state=" + id_state +
                ", state_name='" + state_name + '\'' +
                ", id_countries=" + id_countries +
                ", countries_name='" + countries_name + '\'' +
                ", detail_district=" + detail_district +
                '}';
    }
}

