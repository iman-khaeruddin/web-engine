package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.TransactionList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 02/12/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface TransactinListDao extends PagingAndSortingRepository<TransactionList, Integer> {
    @Query(value = "SELECT * FROM transaction_list WHERE warehouse  = :warehouse", nativeQuery = true)
    @Deprecated
    Page<TransactionList> findByWarehouseName(@Param("warehouse") String warehouse, Pageable pageable);

    @Query(value = "SELECT " +
            " tl.sku, " +
            " tl.customer, " +
            " COUNT( tl.id ) AS frequency, " +
            " SUM( tl.qty ) * - 1 AS qty  " +
            "FROM " +
            " transaction_list tl  " +
            "WHERE " +
            " tl.`status` = 'CLOSE'  " +
            " AND tl.type IN ( 'JOB OUT', 'TRANS OUT' )  " +
            " AND tl.warehouse = :warehouse  " +
            "GROUP BY " +
            " tl.sku, " +
            " tl.customer  " +
            "ORDER BY " +
            " frequency DESC  " +
            " LIMIT 5", nativeQuery = true)
    List<com.mospaze.werehouse.entity.projector.TransactionList> stockStatusWarehouse(@Param("warehouse") String warehouse);

    @Query(value = "SELECT " +
            " tl.sku, " +
            " tl.customer, " +
            " COUNT( tl.id ) AS frequency, " +
            " SUM( tl.qty ) * - 1 AS qty  " +
            "FROM " +
            " transaction_list tl  " +
            "WHERE " +
            " tl.`status` = 'CLOSE'  " +
            " AND tl.type IN ( 'JOB OUT', 'TRANS OUT' )  " +
            "GROUP BY " +
            " tl.sku, " +
            " tl.customer  " +
            "ORDER BY " +
            " frequency DESC  " +
            " LIMIT 5", nativeQuery = true)
    List<com.mospaze.werehouse.entity.projector.TransactionList> stockStatus();
}
