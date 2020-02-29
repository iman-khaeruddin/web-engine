package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseTemperature;
import com.mospaze.werehouse.entity.projector.WarehouseTemperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsWarehouseTemperatureDao extends CrudRepository<MsWarehouseTemperature, Integer> {
    @Query(value = "SELECT DISTINCT" +
            "	mwt.id, " +
            "	mwt.temperature_name " +
            "FROM " +
            "	warehouse w " +
            "JOIN warehouse_temperature wt ON w.id = wt.warehouse_id " +
            "JOIN master_warehouse_temperature mwt ON wt.temperature_id = mwt.id " +
            "WHERE " +
            "	w.id = :id " +
            "AND mwt.status = 1", nativeQuery = true)
    List<WarehouseTemperature> getMsWarehouseTemperatureById(@Param("id") int id);

    @Query(value = "SELECT * FROM master_warehouse_temperature WHERE status = 1", nativeQuery = true)
    Iterable<MsWarehouseTemperature> findAllByActiveStatus();

    @Query(value = "SELECT * FROM master_warehouse_temperature WHERE id IN :list_id", nativeQuery = true)
    List<MsWarehouseTemperature> getWarehouseTemperatureById(@Param("list_id") List<Integer> list_id);
}
