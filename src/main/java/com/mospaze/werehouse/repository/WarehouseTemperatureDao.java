package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseTemperature;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseTemperatureDao extends CrudRepository<WarehouseTemperature, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_temperature WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id);

    @Modifying
    @Query(value = "UPDATE warehouse_temperature SET temperature_id = :temperature_id WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void update(@Param("warehouse_id") int warehouse_id, @Param("temperature_id") int temperature_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_temperature where warehouse_id = :warehouse_id AND temperature_id = :temperature_id", nativeQuery = true)
    int countByTemperatureId(@Param("warehouse_id") int warehouse_id, @Param("temperature_id") int temperature_id);
}
