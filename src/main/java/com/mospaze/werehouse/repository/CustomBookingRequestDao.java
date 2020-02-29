package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.BookingRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface CustomBookingRequestDao {
    /**
     * @param status
     * @param warehouse_name
     * @param modelHasRole
     * @param page
     * @param size
     * @return list of {@link BookingRequest}
     */
    List<BookingRequest> getBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole modelHasRole, Integer page, Integer size);

    /**
     * @param status
     * @param warehouse_name
     * @param modelHasRole
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    BigDecimal getTotalPageBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole modelHasRole, Integer page, Integer size);

    /**
     * @param status
     * @param warehouse_name
     * @param role_login_user
     * @param page
     * @param size
     * @return {@link BigInteger}
     */
    BigInteger getTotalElementBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole role_login_user, Integer page, Integer size);

    /**
     * @param code
     * @return {@link BigInteger}
     */
    BigInteger getCountBookingThisMonth(String code);
}
