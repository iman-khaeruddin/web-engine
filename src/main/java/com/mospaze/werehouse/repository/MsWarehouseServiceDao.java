package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseService;
import com.mospaze.werehouse.entity.projector.WarehouseService;
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
public interface MsWarehouseServiceDao extends PagingAndSortingRepository<MsWarehouseService, Integer> {

	@Query(value = "SELECT DISTINCT" +
			"	mws.id, " + 
			"	mws.services_name, " +
			"	mws.icon, " +
			"	ws.service_price, " +
			"	ws.uom " +
			"FROM " +
			"	warehouse w " +
			"JOIN warehouse_services ws ON w.id = ws.warehouse_id " + 
			"JOIN master_warehouse_service mws ON ws.services_id = mws.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mws.status = 1", nativeQuery = true)
	List<WarehouseService> getMsWarehouseServicesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_service where status = 1", nativeQuery = true)
    Iterable<MsWarehouseService> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_service WHERE services_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
	Page<MsWarehouseService> getMsWarehouseServicesByServicesName(@Param("search_value") String search_value, Pageable pageable);

	@Query(value = "SELECT * FROM master_warehouse_service WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseService> getWarehouseServiceById(@Param("list_id") List<Integer> list_id);
}
