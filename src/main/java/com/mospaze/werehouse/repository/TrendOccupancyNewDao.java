package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.TrendOccupancyNew;
import com.mospaze.werehouse.entity.projector.WarehouseName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 06/12/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface TrendOccupancyNewDao extends CrudRepository<TrendOccupancyNew, Integer> {
    @Query(value = "SELECT * FROM trend_occupancy_new WHERE DATE_FORMAT(date, '%Y-%m') = :month AND user_id = :user_id AND warehouse = :warehouse", nativeQuery = true)
    List<TrendOccupancyNew> findTrendOccupanciesByMonth(@Param("month") String month, @Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT * FROM trend_occupancy_new WHERE DATE_FORMAT(date, '%Y-%m-%d') = :date AND user_id = :user_id AND warehouse = :warehouse", nativeQuery = true)
    List<TrendOccupancyNew> findTrendOccupanciesByDate(@Param("date") String date, @Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT * FROM trend_occupancy_new WHERE user_id = :user_id AND warehouse = :warehouse ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<TrendOccupancyNew> findAll(@Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT DISTINCT " +
            "	w.*  " +
            "FROM " +
            "	warehouse w " +
            "	JOIN trend_occupancy_new tcn ON w.id = tcn.warehouse  " +
            "WHERE " +
            "	tcn.user_id = :user_id", nativeQuery = true)
    List<WarehouseName> getWarehouseRented(@Param("user_id") int user_id);

    @Query(value = "SELECT " +
            " *  " +
            "FROM " +
            " trend_occupancy_new  " +
            "WHERE " +
            " user_id = :user_id  " +
            " AND warehouse = :warehouse  " +
            " AND DATE_FORMAT( date, '%Y-%m-%d' ) = :date", nativeQuery = true)
    Optional<TrendOccupancyNew> validateDuplicateTrendOccupancy(@Param("user_id") int user_id, @Param("warehouse")int warehouse, @Param("date")String date);
}
