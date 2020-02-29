package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.BookingRequest;
import com.mospaze.werehouse.model.*;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface BookingService {
    /**
     * @param bookingRequestModel
     * @param username
     * @return {@link String}
     */
    String bookingProcess(BookingRequestModel bookingRequestModel, String username);

    /**
     * @param status
     * @param warehouse_name
     * @param role_login_user
     * @param page
     * @param size
     * @return all entities {@link BookingRequest} with pageable
     */
    BookingRequestPageable getBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole role_login_user, Integer page, Integer size);

    /**
     * @param id
     * @param role_login_user
     * @return {@link GeneralResponse}
     */
    GeneralResponse getDetailBookingRequestById(int id, ModelHasRole role_login_user);

    /**
     * @param role_login_user
     * @return {@link NotificationResponse}
     */
    @Deprecated
    NotificationResponse getNotificationBooking(ModelHasRole role_login_user);

    /**
     * @param orderRequestModel
     * @param role_login_user
     * @param action
     * @return {@link String}
     */
    String updateStatus(OrderRequestModel orderRequestModel, ModelHasRole role_login_user, int action);
}
