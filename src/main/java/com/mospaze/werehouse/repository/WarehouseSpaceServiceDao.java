package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseSpaceService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseSpaceServiceDao extends CrudRepository<WarehouseSpaceService, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_space_service WHERE warehouse_id = :warehouse_id AND space_service_id = :space_service_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("space_service_id") int space_service_id);

    @Modifying
    @Query(value = "UPDATE warehouse_space_service SET space_service_id = :space_service_id WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void update(@Param("warehouse_id") int warehouse_id, @Param("space_service_id") int space_service_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_space_service where warehouse_id = :warehouse_id AND space_service_id = :space_service_id", nativeQuery = true)
    int countBySpaceServiceId(@Param("warehouse_id") int warehouse_id, @Param("space_service_id") int space_service_id);
}
