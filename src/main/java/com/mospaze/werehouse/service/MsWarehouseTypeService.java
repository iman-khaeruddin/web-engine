package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseType;
import com.mospaze.werehouse.entity.projector.WarehouseType;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseTypeService {

	/**
	 * @param id
	 * @return list of {@link WarehouseType}
	 */
	List<WarehouseType> getMsWarehouseTypesById(int id);

	/**
	 * @return all entities {@link MsWarehouseType}
	 */
	Iterable<MsWarehouseType> getMasterWarehouseType();

	/**
	 * @param warehouseType
	 * @return the saved entity will never be {@literal null}
	 */
	com.mospaze.werehouse.entity.WarehouseType save(com.mospaze.werehouse.entity.WarehouseType warehouseType);

	/**
	 * @param warehouse_id
	 */
	void delete(int warehouse_id);

	/**
	 * @param warehouse_id
	 * @param types_id
	 */
	void update(int warehouse_id, int types_id);

	/**
	 * @param warehouse_id
	 * @param types_id
	 * @return int
	 */
	@Deprecated
	int countByTypeId(int warehouse_id, int types_id);

	/**
	 * @param page
	 * @param size
	 * @param search_value
	 * @return all entities {@link MsWarehouseType} with pageable
	 */
    GeneralResponseNew getMasterType(Integer page, Integer size, String search_value);

	/**
	 * @param id
	 * @return {@link GeneralResponse}
	 */
	GeneralResponse getMasterTypeById(int id);

	/**
	 * @param msWarehouseType
	 * @return {@link String}
	 */
	String updateMasterType(MsWarehouseType msWarehouseType);

	/**
	 * @param msWarehouseType
	 * @return {@link com.mospaze.werehouse.common.Constant}
	 */
	String insertMasterType(MsWarehouseType msWarehouseType);

	/**
	 * @param id
	 * @return {@link String}
	 */
    String deleteMasterType(int id);
}
