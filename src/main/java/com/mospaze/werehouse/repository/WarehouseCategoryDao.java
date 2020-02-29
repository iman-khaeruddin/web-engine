package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseCategoryDao extends CrudRepository<WarehouseCategory, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_category where warehouse_id = :warehouse_id AND category_id = :category_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("category_id") int category_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_category where warehouse_id = :warehouse_id AND category_id = :category_id", nativeQuery = true)
    int countByCategoryId(@Param("warehouse_id") int warehouse_id, @Param("category_id") int category_id);

    @Query(value = "SELECT * FROM warehouse_category WHERE warehouse_id = :warehouse_id AND category_id = :category_id", nativeQuery = true)
    Optional<WarehouseCategory> findByCategoryId(@Param("category_id") int category_id, @Param("warehouse_id") int warehouse_id);
}
