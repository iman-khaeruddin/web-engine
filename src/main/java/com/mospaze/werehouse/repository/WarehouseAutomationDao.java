package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseAutomation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseAutomationDao extends CrudRepository<WarehouseAutomation, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_automation WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id);

    @Modifying
    @Query(value = "UPDATE warehouse_automation SET automation_id = :automation_id WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void update(@Param("warehouse_id") int warehouse_id, @Param("automation_id") int automation_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_automation where warehouse_id = :warehouse_id AND automation_id = :automation_id", nativeQuery = true)
    int countByAutomationId(@Param("warehouse_id") int warehouse_id, @Param("automation_id") int automation_id);
}
