package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.MsWarehouseOperatingHour;
import com.mospaze.werehouse.entity.projector.WarehouseOperatingHour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface MsWarehouseOperatingHourDao extends PagingAndSortingRepository<MsWarehouseOperatingHour, Integer> {
    @Query(value = "SELECT DISTINCT" +
            "	mwc.id, " +
            "	mwc.operation_hour_name, " +
            "	wc.open, " +
            "	wc.close, " +
            "	wc.number_of_shift, " +
            "	wc.regular_overtime, " +
            "	wc.holiday_overtime " +
            "FROM " +
            "	warehouse w " +
            "JOIN warehouse_operating_hour wc ON w.id = wc.operating_hour_id " +
            "JOIN master_warehouse_operating_hour mwc ON wc.operating_hour_id = mwc.id " +
            "WHERE " +
            "	w.id = :id " +
            "AND mwc.status = 1", nativeQuery = true)
    List<WarehouseOperatingHour> getMsWarehouseOperatingHourById(@Param("id") int id);

    @Query(value = "SELECT * FROM master_warehouse_operating_hour WHERE operation_hour_name LIKE CONCAT('%',:search_value,'%')", nativeQuery = true)
    Page<MsWarehouseOperatingHour> getMsWarehouseOperatingByOperationName(@Param("search_value") String search_value, Pageable pageable);
}
