package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseFeature;
import com.mospaze.werehouse.entity.MsWarehouseSpaceService;
import com.mospaze.werehouse.entity.projector.WarehouseSpaceService;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseSpaceServiceService {

	/**
	 * @param id
	 * @return list of {@link WarehouseSpaceService}
	 */
	List<WarehouseSpaceService> getMsWarehouseSpaceServicesById(int id);

	/**
	 * @return all entities {@link MsWarehouseSpaceService}
	 */
	Iterable<MsWarehouseSpaceService> getMasterWarehouseSpaceService();

	/**
	 * @param warehouseSpaceService
	 * @return the saved entity will never be {@literal null}
	 */
	com.mospaze.werehouse.entity.WarehouseSpaceService save(com.mospaze.werehouse.entity.WarehouseSpaceService warehouseSpaceService);

	/**
	 * @param warehouse_id
	 * @param space_service_id
	 */
	void delete(int warehouse_id, int space_service_id);

	/**
	 * @param warehouse_id
	 * @param space_service_id
	 */
	void update(int warehouse_id, int space_service_id);

	/**
	 * @param warehouse_id
	 * @param space_service_id
	 * @return {@link Integer}
	 */
	int countBySpaceServiceId(int warehouse_id, int space_service_id);
}
