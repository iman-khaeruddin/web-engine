package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.BookingRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface BookingRequestDao extends PagingAndSortingRepository<BookingRequest, Integer> {

    /**
     * @param status
     * @param model_id
     * @return {@link Integer}
     */
    @Query(value = "SELECT  " +
            " count( br.id )   " +
            "FROM  " +
            " booking_request br  " +
            " JOIN warehouse w ON br.warehouse_id = w.id   " +
            "WHERE  " +
            " br.STATUS = :status   " +
            " AND w.user_id = :model_id", nativeQuery = true)
    int getCountBookingRequestByStatusSeller(@Param("status") int status, @Param("model_id") int model_id);

    /**
     * @param buyer_id
     * @return {@link Integer}
     */
    @Query(value = "SELECT count(id) FROM booking_request WHERE status in (3) AND buyer_id = :buyer_id", nativeQuery = true)
    int getCountBookingRequestByStatusBuyer(@Param("buyer_id") int buyer_id);

    /**
     * @param status
     * @return {@link Integer}
     */
    @Query(value = "SELECT count(id) FROM booking_request WHERE status = :status", nativeQuery = true)
    int getCountBookingRequestByStatusAdmin(@Param("status") int status);
}
