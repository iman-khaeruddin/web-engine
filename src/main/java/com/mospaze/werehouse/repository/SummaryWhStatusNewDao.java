package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.SummaryWhStatusNew;
import com.mospaze.werehouse.entity.projector.WarehouseName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface SummaryWhStatusNewDao extends CrudRepository<SummaryWhStatusNew, Integer> {
    @Query(value = "SELECT * FROM summary_wh_status_new WHERE DATE_FORMAT(date, '%Y-%m') = :month AND user_id = :user_id AND warehouse = :warehouse", nativeQuery = true)
    List<SummaryWhStatusNew> findSummaryWhStatusByMonth(@Param("month") String month, @Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT * FROM summary_wh_status_new WHERE DATE_FORMAT(date, '%Y-%m-%d') = :date AND user_id = :user_id AND warehouse = :warehouse", nativeQuery = true)
    List<SummaryWhStatusNew> findSummaryWhStatusByDate(@Param("date") String date, @Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT * FROM summary_wh_status_new WHERE user_id = :user_id AND warehouse = :warehouse ORDER BY id DESC LIMIT 1", nativeQuery = true)
    List<SummaryWhStatusNew> findAll(@Param("user_id") int user_id, @Param("warehouse") int warehouse);

    @Query(value = "SELECT DISTINCT " +
            "	w.*  " +
            "FROM " +
            "	warehouse w " +
            "	JOIN summary_wh_status_new tcn ON w.id = tcn.warehouse  " +
            "WHERE " +
            "	tcn.user_id = :user_id", nativeQuery = true)
    List<WarehouseName> getWarehouseRented(@Param("user_id") int user_id);

    @Query(value = "SELECT " +
            " *  " +
            "FROM " +
            " summary_wh_status_new  " +
            "WHERE " +
            " user_id = :user_id  " +
            " AND warehouse = :warehouse  " +
            " AND DATE_FORMAT( date, '%Y-%m-%d' ) = :date", nativeQuery = true)
    Optional<SummaryWhStatusNew> validateDuplicateSummaryWhStatus(@Param("user_id") int user_id, @Param("warehouse")int warehouse, @Param("date") String date);
}
