package com.mospaze.werehouse.service;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseFeature;
import com.mospaze.werehouse.entity.WarehouseFeatures;
import com.mospaze.werehouse.entity.projector.WarehouseFeature;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface MsWarehouseFeatureService {

	/**
	 * @param id
	 * @return list of {@link WarehouseFeature}
	 */
	List<WarehouseFeature> getMsWarehouseFeaturesById(int id);

	/**
	 * @return all entities {@link MsWarehouseFeature}
	 */
	Iterable<MsWarehouseFeature> getMasterWarehouseFeature();

	/**
	 * @param warehouseFeatures
	 * @return the saved entity will never be {@literal null}
	 */
	WarehouseFeatures save(WarehouseFeatures warehouseFeatures);

	/**
	 * @param warehouse_id
	 * @param features_id
	 */
	void delete(int warehouse_id, int features_id);

	/**
	 * @param warehouse_id
	 * @param features_id
	 * @return {@link Integer}
	 */
	int countByFeatureId(int warehouse_id, int features_id);

	/**
	 * @param msWarehouseFeature
	 * @return {@link String}
	 */
	String updateMasterFeature(MsWarehouseFeature msWarehouseFeature);

	/**
	 * @param id
	 * @return {@link GeneralResponse}
	 */
    GeneralResponse getMasterFeatureById(int id);

	/**
	 * @param page
	 * @param size
	 * @param search_value
	 * @return all entities {@link MsWarehouseFeature} with pageable
	 */
    GeneralResponseNew getMasterFeature(Integer page, Integer size, String search_value);

	/**
	 * @param msWarehouseFeature
	 * @return {@link String}
	 */
    String insertMasterFeature(MsWarehouseFeature msWarehouseFeature);

	/**
	 * @param id
	 * @return {@link String}
	 */
    String deleteMasterFeature(int id);
}
