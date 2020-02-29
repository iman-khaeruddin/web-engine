package com.mospaze.werehouse.repository;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 03/12/2019
 */
public interface CustomStockOnHandDao {
    @Deprecated
    List<Object[]> findStockOnHand(String month, String date, String warehouse);

    List<Object[]> findStockOnHandNew(String month, String date, int warehouse, int user_id, String start_date, String end_date);
}
