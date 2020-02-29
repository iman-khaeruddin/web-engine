package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseAutomation;
import com.mospaze.werehouse.entity.projector.WarehouseAutomation;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseAutomationService {

	/**
	 * @param id
	 * @return list of {@link WarehouseAutomation}
	 */
	List<WarehouseAutomation> getMsWarehouseAutomationsById(int id);

	/**
	 * @return all entities {@link MsWarehouseAutomation}
	 */
	Iterable<MsWarehouseAutomation> getMasterWarehouseAutomation();

	/**
	 * @param warehouseAutomation
	 * @return the saved entity will never be {@literal null}
	 */
	com.mospaze.werehouse.entity.WarehouseAutomation save(com.mospaze.werehouse.entity.WarehouseAutomation warehouseAutomation);

	/**
	 * @param warehouse_id
	 */
	void delete(int warehouse_id);

	/**
	 * @param warehouse_id
	 * @param automation_id
	 */
	void update(int warehouse_id, int automation_id);

	/**
	 * @param warehouse_id
	 * @param automation_id
	 * @return {@link Integer}
	 */
	@Deprecated
    int countByAutomationId(int warehouse_id, int automation_id);

	/**
	 * @param page
	 * @param size
	 * @param search_value
	 * @return all entities {@link MsWarehouseAutomation} with pageable
	 */
    GeneralResponseNew getMasterAutomation(Integer page, Integer size, String search_value);

	/**
	 * @param id
	 * @return {@link GeneralResponse}
	 */
	GeneralResponse getMasterAutomationById(int id);

	/**
	 * @param msWarehouseAutomation
	 * @return {@link String}
	 */
    String updateMasterAutomation(MsWarehouseAutomation msWarehouseAutomation);

	/**
	 * @param msWarehouseAutomation
	 * @return {@link String}
	 */
	String insertMasterAutomation(MsWarehouseAutomation msWarehouseAutomation);

	/**
	 * @param id
	 * @return {@link String}
	 */
    String deleteMasterAutomation(int id);
}
