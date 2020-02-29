package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.WarehouseService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface WarehouseServiceDao extends CrudRepository<WarehouseService, Integer> {
    @Modifying
    @Query(value = "DELETE FROM warehouse_services where warehouse_id = :warehouse_id AND services_id = :services_id", nativeQuery = true)
    void delete(@Param("warehouse_id") int warehouse_id, @Param("services_id") int services_id);

    @Query(value = "SELECT count(id) as count FROM warehouse_services where warehouse_id = :warehouse_id and services_id = :services_id", nativeQuery = true)
    int countByServiceId(@Param("warehouse_id") int warehouse_id, @Param("services_id") int services_id);

    @Query(value = "SELECT * FROM warehouse_services WHERE services_id = :services_id AND warehouse_id = :warehouse_id LIMIT 1", nativeQuery = true)
    Optional<WarehouseService> findByServicesId(@Param("services_id") int services_id, @Param("warehouse_id") int warehouse_id);
}
