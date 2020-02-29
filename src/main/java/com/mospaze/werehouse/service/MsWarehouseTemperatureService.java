package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.MsWarehouseTemperature;
import com.mospaze.werehouse.entity.projector.WarehouseTemperature;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseTemperatureService {
    /**
     * @param id
     * @return list of {@link WarehouseTemperature}
     */
    List<WarehouseTemperature> getMsWarehouseTemperatureById(int id);

    /**
     * @return all entities {@link MsWarehouseTemperature}
     */
    Iterable<MsWarehouseTemperature> getMasterWarehouseTemperature();

    /**
     * @param warehouseTemperature
     * @return the saved entity will never be {@literal null}
     */
    com.mospaze.werehouse.entity.WarehouseTemperature save(com.mospaze.werehouse.entity.WarehouseTemperature warehouseTemperature);

    /**
     * @param warehouse_id
     */
    void delete(int warehouse_id);

    /**
     * @param warehouse_id
     * @param temperature_id
     */
    void update(int warehouse_id, int temperature_id);

    /**
     * @param warehouse_id
     * @param temperature_id
     * @return {@link Integer}
     */
    @Deprecated
    int countByTemperatureId(int warehouse_id, int temperature_id);
}
