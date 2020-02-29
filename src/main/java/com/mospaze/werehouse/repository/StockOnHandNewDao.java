package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.StockOnHandNew;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface StockOnHandNewDao extends CrudRepository<StockOnHandNew, Integer> {
    @Query(value = "SELECT " +
            " *  " +
            "FROM " +
            " stock_on_hand_new  " +
            "WHERE " +
            " user_id = :user_id  " +
            " AND warehouse = :warehouse  " +
            " AND sku = :sku  " +
            " AND pallet_num = :pallet_num  " +
            " AND primary_key = :primary_key  " +
            " AND DATE_FORMAT( receive_date, '%Y-%m-%d' ) = :receive_date " +
            " AND DATE_FORMAT( date, '%Y-%m-%d' ) = :date", nativeQuery = true)
    Optional<StockOnHandNew> validateDuplicateStockOnHand(@Param("user_id") int user_id, @Param("warehouse") int warehouse, @Param("receive_date") String receive_date, @Param("pallet_num") String pallet_num, @Param("sku") String sku, @Param("primary_key") String primary_key, @Param("date") String date);
}
