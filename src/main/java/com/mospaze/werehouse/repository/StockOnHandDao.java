package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.StockOnHand;
import com.mospaze.werehouse.entity.projector.WarehouseName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface StockOnHandDao extends CrudRepository<StockOnHand, Integer> {
    @Query(value = "SELECT " +
            "  COUNT( soh.id ) AS jml, " +
            "  soh.description AS product_family, " +
            "  SUM( soh.qty_stock ) AS qty, " +
            "  soh.gedung AS warehouse  " +
            "FROM " +
            "  stock_on_hand soh  " +
            "WHERE " +
            " soh.month = :month  " +
            "GROUP BY " +
            "  soh.description, " +
            "  soh.gedung", nativeQuery = true)
    @Deprecated
    List<com.mospaze.werehouse.entity.projector.StockOnHand> findStockOnHandByMonth(@Param("month") String month);

    @Query(value = "SELECT " +
            "  COUNT( soh.id ) AS jml, " +
            "  soh.description AS product_family, " +
            "  SUM( soh.qty_stock ) AS qty, " +
            "  soh.gedung AS warehouse  " +
            "FROM " +
            "  stock_on_hand soh  " +
            "GROUP BY " +
            "  soh.description, " +
            "  soh.gedung", nativeQuery = true)
    @Deprecated
    List<com.mospaze.werehouse.entity.projector.StockOnHand> findAllStockOnHand();

    @Query(value = "SELECT " +
            "  COUNT( soh.id ) AS jml, " +
            "  soh.description AS product_family, " +
            "  SUM( soh.qty_stock ) AS qty, " +
            "  soh.gedung AS warehouse  " +
            "FROM " +
            "  stock_on_hand soh  " +
            "WHERE " +
            " soh.date = :date  " +
            "GROUP BY " +
            "  soh.description, " +
            "  soh.gedung", nativeQuery = true)
    @Deprecated
    List<com.mospaze.werehouse.entity.projector.StockOnHand> findStockOnHandByDate(@Param("date") String date);

    @Query(value = "SELECT DISTINCT " +
            "	w.*  " +
            "FROM " +
            "	warehouse w " +
            "	JOIN stock_on_hand_new tcn ON w.id = tcn.warehouse  " +
            "WHERE " +
            "	tcn.user_id = :user_id", nativeQuery = true)
    List<WarehouseName> getWarehouseRented(@Param("user_id") int user_id);
}
