package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseCategory;
import com.mospaze.werehouse.entity.projector.WarehouseCategory;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseCategoryService {

	/**
	 * @param id
	 * @return list of {@link WarehouseCategory}
	 */
	List<WarehouseCategory> getMsWarehouseCategoriesById(int id);

	/**
	 * @return all entities {@link MsWarehouseCategory}
	 */
	Iterable<MsWarehouseCategory> getMasterWarehouseCategory();

	/**
	 * @param warehouseCategory
	 * @return the saved entity will never be {@literal null}
	 */
	com.mospaze.werehouse.entity.WarehouseCategory save(com.mospaze.werehouse.entity.WarehouseCategory warehouseCategory);

	/**
	 * @param warehouse_id
	 * @param category_id
	 */
	void delete(int warehouse_id, int category_id);

	/**
	 * @param warehouse_id
	 * @param category_id
	 * @return {@link Integer}
	 */
	int countByCategoryId(int warehouse_id, int category_id);

	/**
	 * @param page
	 * @param size
	 * @param search_value
	 * @return all entities {@link MsWarehouseCategory} with pageable
	 */
    GeneralResponseNew getMasterCategory(Integer page, Integer size, String search_value);

	/**
	 * @param id
	 * @return {@link GeneralResponse}
	 */
	GeneralResponse getMasterCategoryById(int id);

	/**
	 * @param msWarehouseCategory
	 * @return {@link String}
	 */
	String updateMasterCategory(MsWarehouseCategory msWarehouseCategory);

	/**
	 * @param msWarehouseCategory
	 * @return {@link String}
	 */
    String insertMasterCategory(MsWarehouseCategory msWarehouseCategory);

	/**
	 * @param id
	 * @return {@link String}
	 */
    String deleteMasterCategory(int id);
}
