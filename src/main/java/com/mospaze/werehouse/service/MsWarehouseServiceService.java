package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.MsWarehouseService;
import com.mospaze.werehouse.entity.WarehouseService;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseServiceService {

    /**
     * @param id
     * @return list of {@link com.mospaze.werehouse.entity.projector.WarehouseService}
     */
	List<com.mospaze.werehouse.entity.projector.WarehouseService> getMsWarehouseServicesById(int id);

    /**
     * @return all entities {@link MsWarehouseService}
     */
	Iterable<MsWarehouseService> getMasterWarehouseService();

    /**
     * @param warehouseService
     * @return the saved entity will never be {@literal null}
     */
	WarehouseService save(WarehouseService warehouseService);

    /**
     * @param warehouse_id
     * @param service_id
     */
	void delete(int warehouse_id, int service_id);

    /**
     * @param warehouse_id
     * @param service_id
     * @return {@link Integer}
     */
	int countByServiceId(int warehouse_id, int service_id);

    /**
     * @param page
     * @param size
     * @param search_value
     * @return list of {@link MsWarehouseService} with pageable
     */
    GeneralResponseNew getMasterService(Integer page, Integer size, String search_value);

    /**
     * @param id
     * @return {@link GeneralResponse}
     */
    GeneralResponse getMasterServiceById(int id);

    /**
     * @param msWarehouseService
     * @return {@link String}
     */
    String updateMasterService(MsWarehouseService msWarehouseService);

    /**
     * @param msWarehouseService
     * @return {@link String}
     */
    String insertMasterService(MsWarehouseService msWarehouseService);

    /**
     * @param id
     * @return {@link String}
     */
    String deleteMasterService(int id);
}
