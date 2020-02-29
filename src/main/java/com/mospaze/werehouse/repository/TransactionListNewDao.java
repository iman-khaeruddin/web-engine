package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.TransactionListNew;
import com.mospaze.werehouse.entity.projector.TransactionList;
import com.mospaze.werehouse.entity.projector.WarehouseName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 02/12/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface TransactionListNewDao extends PagingAndSortingRepository<TransactionListNew, Integer> {

    @Query(value = "SELECT " +
            " tl.sku, " +
            " s.company as customer, " +
            " COUNT( tl.id ) AS frequency, " +
            " SUM( tl.qty ) AS qty  " +
            "FROM " +
            " transaction_list_new tl  " +
            "JOIN " +
            " users s ON s.id = tl.user_id  " +
            "WHERE " +
            " tl.`status` = 'CLOSE'  " +
            " AND tl.type IN ( 'JOB OUT', 'TRANS OUT' )  " +
            " AND tl.warehouse = :warehouse  " +
            " AND tl.user_id = :user_id  " +
            "GROUP BY " +
            " tl.sku, " +
            " s.company " +
            "ORDER BY " +
            " frequency DESC  " +
            " LIMIT 5", nativeQuery = true)
    List<TransactionList> stockStatusWarehouse(@Param("warehouse") int warehouse, @Param("user_id") int user_id);

    @Query(value = "SELECT DISTINCT " +
            "	w.*  " +
            "FROM " +
            "	warehouse w " +
            "	JOIN transaction_list_new tcn ON w.id = tcn.warehouse  " +
            "WHERE " +
            "	tcn.user_id = :user_id", nativeQuery = true)
    List<WarehouseName> getWarehouseRented(@Param("user_id") int user_id);

    @Query(value = "SELECT " +
            " *  " +
            "FROM " +
            " transaction_list_new  " +
            "WHERE " +
            " user_id = :user_id  " +
            " AND warehouse = :warehouse  " +
            " AND sku = :sku  " +
            " AND job_no = :job_no  " +
            " AND batch = :batch  " +
            " AND location = :location  " +
            " AND DATE_FORMAT( create_date, '%Y-%m-%d' ) = :create_date", nativeQuery = true)
    Optional<TransactionListNew> validateDuplicateTransactionList(@Param("user_id") int user_id, @Param("warehouse") int warehouse, @Param("sku") String sku, @Param("job_no") String job_no, @Param("create_date") String create_date, @Param("location") String location, @Param("batch") String batch);
}
