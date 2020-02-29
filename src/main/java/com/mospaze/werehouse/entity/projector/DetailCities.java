package com.mospaze.werehouse.entity.projector;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface DetailCities {
    String getId_district();
    String getDistrict_name();
    String getId_cities();
    String getCities_name();
    String getId_state();
    String getState_name();
    String getId_countries();
    String getCountries_name();
    String getPostcode();
}
