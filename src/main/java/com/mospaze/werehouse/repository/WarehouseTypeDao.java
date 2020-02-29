package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseTypeDao extends CrudRepository<WarehouseType, Integer> {
    /**
     * Delete the entity with the given warehouse_id
     *
     * @param warehouse_id
     */
    @Modifying
    @Query(value = "DELETE FROM warehouse_types WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id);

    /**
     * Update the entity with the given params
     *
     * @param warehouse_id
     * @param types_id
     */
    @Modifying
    @Query(value = "UPDATE warehouse_types SET types_id = :types_id WHERE warehouse_id = :warehouse_id", nativeQuery = true)
    void update(@Param("warehouse_id") int warehouse_id, @Param("types_id") int types_id);

    /**
     * Returns count an entity with the given params
     *
     * @param warehouse_id
     * @param types_id
     * @return {@literal Integer} if an entity with the given params
     */
    @Query(value = "SELECT count(id) as count FROM warehouse_types where warehouse_id = :warehouse_id AND types_id = :types_id", nativeQuery = true)
    int countByTypeId(@Param("warehouse_id") int warehouse_id, @Param("types_id") int types_id);
}
