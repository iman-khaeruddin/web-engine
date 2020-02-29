package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseOperatingHour;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseOperatingHourDao extends CrudRepository<WarehouseOperatingHour, Integer> {
    @Query(value = "SELECT count(id) as count FROM warehouse_operating_hour where warehouse_id = :warehouse_id AND operating_hour_id = :operating_hour_id", nativeQuery = true)
    int countByOperationHourId(@Param("warehouse_id") int warehouse_id, @Param("operating_hour_id") int operation_hour_id);

    @Modifying
    @Query(value = "DELETE FROM warehouse_operating_hour where warehouse_id = :warehouse_id AND operating_hour_id = :operating_hour_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("operating_hour_id") int operation_hour_id);

    @Query(value = "SELECT * FROM warehouse_operating_hour where warehouse_id = :warehouse_id AND operating_hour_id = :operating_hour_id", nativeQuery = true)
    Optional<WarehouseOperatingHour> findByOperatingHourId(@Param("warehouse_id") int warehouse_id, @Param("operating_hour_id") int operation_hour_id);
}
