package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseFeatures;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseFeaturesDao extends CrudRepository<WarehouseFeatures, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_features where warehouse_id = :warehouse_id AND features_id = :features_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("features_id") int features_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_features where warehouse_id = :warehouse_id and features_id = :features_id", nativeQuery = true)
    int countByFeatureId(@Param("warehouse_id") int warehouse_id, @Param("features_id") int features_id);
}
