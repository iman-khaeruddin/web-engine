package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseSpaceService;
import com.mospaze.werehouse.entity.projector.WarehouseSpaceService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsWarehouseSpaceServiceDao extends CrudRepository<MsWarehouseSpaceService, Integer>{

	@Query(value = "SELECT DISTINCT" +
			"	mwss.id, " + 
			"	mwss.space_service_name " + 
			"FROM " + 
			"	warehouse w " +
			"JOIN warehouse_space_service wss ON w.id = wss.warehouse_id " + 
			"JOIN master_warehouse_space_service mwss ON wss.space_service_id = mwss.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mwss.status = 1", nativeQuery = true)
	List<WarehouseSpaceService> getMsWarehouseSpaceServicesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_space_service WHERE status = 1", nativeQuery = true)
    Iterable<MsWarehouseSpaceService> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_space_service WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseSpaceService> getWarehouseSpaceServiceById(@Param("list_id") List<Integer> list_id);
}
