package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.FulfillmentSpace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface FulfillmentSpaceDao extends CrudRepository<FulfillmentSpace, Integer> {
    /**
     * @param booking_request_id
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    @Query(value = "SELECT * FROM fulfillment_space WHERE booking_request_id = :booking_request_id", nativeQuery = true)
    Optional<FulfillmentSpace> findByBookingRequestId(@Param("booking_request_id") int booking_request_id);
}
