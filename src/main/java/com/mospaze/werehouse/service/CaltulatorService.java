package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.GetNumber;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface CaltulatorService {
    /**
     * @param unit
     * @param id_cities
     * @param temperature
     * @return {@link GetNumber}
     */
    GetNumber getMaxSize(Integer unit, Integer id_cities, Integer temperature);

    /**
     * @param unit
     * @param id_cities
     * @param temperature
     * @param size
     * @param periode
     * @return {@link GetNumber}
     */
    GetNumber getAverage(Integer unit, Integer id_cities, Integer temperature, Integer size, Integer periode);
}
