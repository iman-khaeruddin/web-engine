package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.SummaryWhStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface SummaryWhStatusDao extends CrudRepository<SummaryWhStatus, Integer> {
    @Query(value = "SELECT * FROM summary_wh_status WHERE month = :month", nativeQuery = true)
    List<SummaryWhStatus> findSummaryWhStatusByMonth(@Param("month") String month);

    @Query(value = "SELECT * FROM summary_wh_status WHERE date = :date", nativeQuery = true)
    List<SummaryWhStatus> findSummaryWhStatusByDate(@Param("date") String date);
}
