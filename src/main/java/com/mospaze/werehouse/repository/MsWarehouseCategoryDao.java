package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseCategory;
import com.mospaze.werehouse.entity.projector.WarehouseCategory;
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
public interface MsWarehouseCategoryDao extends PagingAndSortingRepository<MsWarehouseCategory, Integer> {

	@Query(value = "SELECT DISTINCT" +
			"	mwc.id, " + 
			"	mwc.category_name, " +
			"	wc.inbound_price, " +
			"	wc.outbound_price, " +
			"	wc.inbound_uom, " +
			"	wc.outbound_uom " +
			"FROM " +
			"	warehouse w " + 
			"JOIN warehouse_category wc ON w.id = wc.warehouse_id " + 
			"JOIN master_warehouse_category mwc ON wc.category_id = mwc.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mwc.status = 1", nativeQuery = true)
	List<WarehouseCategory> getMsWarehouseCategoriesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_category where status = 1", nativeQuery = true)
    Iterable<MsWarehouseCategory> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_category WHERE category_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
	Page<MsWarehouseCategory> getMsWarehouseTypesByCategoryName(@Param("search_value") String search_value, Pageable pageable);

	@Query(value = "SELECT * FROM master_warehouse_category WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseCategory> getWarehouseCategoryById(@Param("list_id") List<Integer> list_id);
}
