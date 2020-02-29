package com.mospaze.werehouse.service;

import com.mospaze.werehouse.entity.Notification;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.NotificationPageable;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface NotificationService {
    /**
     * @param notification
     * @return {@link Notification}
     */
    Notification insertNotification(Notification notification);

    /**
     * @param user_id
     * @param type
     * @param page
     * @param size
     * @return list of {@link Notification} with pageable
     */
    NotificationPageable getNotification(Integer user_id, Integer type, Integer page, Integer size);

    /**
     * @param id
     * @param jwt
     * @return {@link String}
     */
    String changeStatusUnRead(int id, JWT jwt);

    /**
     * @param id
     * @param jwt
     * @return {@link String}
     */
    String changeStatusRead(int id, JWT jwt);
}
