package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.MsWarehouseOperatingHour;
import com.mospaze.werehouse.entity.WarehouseOperatingHour;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 */
public interface MsWarehouseOperatingHourService {

    /**
     * @param warehouseOperatingHour
     * @return the saved entity will never be {@literal null}
     */
    WarehouseOperatingHour save(WarehouseOperatingHour warehouseOperatingHour);

    /**
     * @param id
     * @return list of {@link com.mospaze.werehouse.entity.projector.WarehouseOperatingHour}
     */
    List<com.mospaze.werehouse.entity.projector.WarehouseOperatingHour> getMsWarehouseOperationHourById(int id);

    /**
     * @param warehouse_id
     * @param operation_hour_id
     * @return {@link Integer}
     */
    int countByOperationHourId(int warehouse_id, int operation_hour_id);

    /**
     * @param warehouse_id
     * @param operation_hour_id
     */
    void delete(int warehouse_id, int operation_hour_id);

    /**
     * @param page
     * @param size
     * @param search_value
     * @return all entities {@link com.mospaze.werehouse.entity.MsWarehouseOperatingHour} with pageable
     */
    GeneralResponseNew getMasterOperatingHour(Integer page, Integer size, String search_value);

    /**
     * @return all entities {@link MsWarehouseOperatingHour}
     */
    Iterable<MsWarehouseOperatingHour> getMasterWarehouseOperatingHour();

    /**
     * @param id
     * @return {@link GeneralResponse}
     */
    GeneralResponse getMasterOperatingHourById(int id);

    /**
     * @param msWarehouseOperatingHour
     * @return {@link String}
     */
    String updateMasterOperatingHour(MsWarehouseOperatingHour msWarehouseOperatingHour);

    /**
     * @param msWarehouseOperatingHour
     * @return {@link String}
     */
    String insertMasterOperatingHour(MsWarehouseOperatingHour msWarehouseOperatingHour);

    /**
     * @param id
     * @return {@link String}
     */
    String deleteMasterOperatingHour(int id);
}
