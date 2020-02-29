package com.mospaze.werehouse.repository;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 */
public interface CustomDataTrackerDao {

    /**
     * @param month
     * @param date
     * @return list of array of {@link Object}
     */
    List<Object[]> findDataTrackerByOs(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of array of {@link Object}
     */
    List<Object[]> findDataTrackerByBrowser(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of array of {@link Object}
     */
    List<Object[]> findDataTrackerByIpAddress(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of array of {@link Object}
     */
    List<Object[]> findDataTrackerByCity(String month, String date);

    /**
     * @param month
     * @param date
     * @return list of array of {@link Object}
     */
    List<Object[]> findDataTrackerByDistrict(String month, String date);
}
