package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseSpace;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseSpaceDao extends CrudRepository<WarehouseSpace, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_space where warehouse_id = :warehouse_id AND space_id = :space_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("space_id") int space_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_space where warehouse_id = :warehouse_id AND space_id = :space_id", nativeQuery = true)
    int countBySpaceId(@Param("warehouse_id") int warehouse_id, @Param("space_id") int space_id);
}
