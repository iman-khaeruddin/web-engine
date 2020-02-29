package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseFeature;
import com.mospaze.werehouse.entity.projector.WarehouseFeature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface MsWarehouseFeatureDao extends PagingAndSortingRepository<MsWarehouseFeature, Integer> {

	@Query(value = "SELECT DISTINCT" +
			"	mwf.id, " + 
			"	mwf.features_name, " +
			"	mwf.icon " +
			"FROM " +
			"	warehouse w " +
			"JOIN warehouse_features wf ON w.id = wf.warehouse_id " + 
			"JOIN master_warehouse_features mwf ON wf.features_id = mwf.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mwf.status = 1", nativeQuery = true)
	List<WarehouseFeature> getMsWarehouseFeaturesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_features where status = 1", nativeQuery = true)
    Iterable<MsWarehouseFeature> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_features WHERE features_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
	Page<MsWarehouseFeature> getMsWarehouseFeaturesByFeatureName(@Param("search_value") String search_value, Pageable pageable);

	@Query(value = "SELECT * FROM master_warehouse_features WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseFeature> getWarehouseFeatureById(@Param("list_id") List<Integer> list_id);
}
