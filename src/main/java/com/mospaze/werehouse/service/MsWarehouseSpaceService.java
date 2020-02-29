package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseFeature;
import com.mospaze.werehouse.entity.MsWarehouseSpace;
import com.mospaze.werehouse.entity.projector.WarehouseSpace;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseSpaceService {

	/**
	 * @param id
	 * @return list of {@link WarehouseSpace}
	 */
	List<WarehouseSpace> getMsWarehouseSpacesById(int id);

	/**
	 * @return all entities {@link MsWarehouseSpace}
	 */
	Iterable<MsWarehouseSpace> getMasterWarehouseSpace();

	/**
	 * @param warehouseSpace
	 * @return the saved entity will never be {@literal null}
	 */
	com.mospaze.werehouse.entity.WarehouseSpace save(com.mospaze.werehouse.entity.WarehouseSpace warehouseSpace);

	/**
	 * @param warehouse_id
	 * @param space_id
	 */
	void delete(int warehouse_id, int space_id);

	/**
	 * @param warehouse_id
	 * @param space_id
	 * @return {@link Integer}
	 */
	int countBySpaceId(int warehouse_id, int space_id);
}
