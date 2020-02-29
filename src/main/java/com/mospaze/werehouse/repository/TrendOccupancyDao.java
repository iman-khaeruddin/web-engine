package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.TrendOccupancy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface TrendOccupancyDao extends CrudRepository<TrendOccupancy, Integer> {
    @Query(value = "SELECT * FROM trend_occupancy WHERE month = :month", nativeQuery = true)
    List<TrendOccupancy> findTrendOccupanciesByMonth(@Param("month") String month);

    @Query(value = "SELECT * FROM trend_occupancy WHERE date = :date", nativeQuery = true)
    List<TrendOccupancy> findTrendOccupanciesByDate(@Param("date") String date);
}
