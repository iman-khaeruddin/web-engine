package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseType;
import com.mospaze.werehouse.entity.projector.WarehouseType;
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
public interface MsWarehouseTypeDao extends PagingAndSortingRepository<MsWarehouseType, Integer> {

	@Query(value = "SELECT DISTINCT" +
			"	mwt.id, " + 
			"	mwt.type_name " + 
			"FROM " + 
			"	warehouse w " +
			"JOIN warehouse_types wt ON w.id = wt.warehouse_id " + 
			"JOIN master_warehouse_type mwt ON wt.types_id = mwt.id " + 
			"WHERE " + 
			"	w.id = :id " + 
			"AND mwt.status = 1", nativeQuery = true)
	List<WarehouseType> getMsWarehouseTypesById(@Param("id") int id);

	@Query(value = "SELECT * FROM master_warehouse_type WHERE status = 1", nativeQuery = true)
    Iterable<MsWarehouseType> findAllByActiveStatus();

	@Query(value = "SELECT * FROM master_warehouse_type WHERE type_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
	Page<MsWarehouseType> getMsWarehouseTypesByTypeName(@Param("search_value") String search_value, Pageable pageable);

	@Query(value = "SELECT * FROM master_warehouse_type WHERE id IN :list_id", nativeQuery = true)
	List<MsWarehouseType> getWarehouseTypeById(@Param("list_id") List<Integer> list_id);
}
