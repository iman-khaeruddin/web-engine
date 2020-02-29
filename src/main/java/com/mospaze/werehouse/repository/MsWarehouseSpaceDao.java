package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseSpace;
import com.mospaze.werehouse.entity.projector.WarehouseSpace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface MsWarehouseSpaceDao extends CrudRepository<MsWarehouseSpace, Integer>{

	@Query(value = "SELECT DISTINCT" +
			"	mws.id, " + 
			"	mws.space_name " + 
			"FROM " + 
			"	warehouse w " +
			"JOIN warehouse_space ws ON w.id = ws.warehouse_id " + 
			"JOIN master_warehouse_space mws ON ws.space_id = mws.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mws.status = 1", nativeQuery = true)
	List<WarehouseSpace> getMsWarehouseSpacesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_space WHERE status = 1", nativeQuery = true)
    Iterable<MsWarehouseSpace> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_space WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseSpace> getWarehouseSpaceById(@Param("list_id") List<Integer> list_id);
}
