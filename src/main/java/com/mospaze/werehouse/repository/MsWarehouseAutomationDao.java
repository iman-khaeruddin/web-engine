package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseAutomation;
import com.mospaze.werehouse.entity.projector.WarehouseAutomation;
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
public interface MsWarehouseAutomationDao extends PagingAndSortingRepository<MsWarehouseAutomation, Integer> {

	@Query(value = "SELECT DISTINCT" +
			"	mwa.id, " + 
			"	mwa.automation_name " + 
			"FROM " + 
			"	warehouse w " +
			"JOIN warehouse_automation wa ON w.id = wa.warehouse_id " + 
			"JOIN master_warehouse_automation mwa ON wa.automation_id = mwa.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mwa.status = 1", nativeQuery = true)
	List<WarehouseAutomation> getMsWarehouseAutomationsById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_automation WHERE status = 1", nativeQuery = true)
    Iterable<MsWarehouseAutomation> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_automation WHERE automation_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
	Page<MsWarehouseAutomation> getMsWarehouseAutomationsByAutomationName(@Param("search_value") String search_value, Pageable pageable);

	@Query(value = "SELECT * FROM master_warehouse_automation WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseAutomation> getWarehouseAutomationById(@Param("list_id") List<Integer> list_id);
}
