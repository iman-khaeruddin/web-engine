package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface NotificationDao extends PagingAndSortingRepository<Notification, Integer> {
    @Query(value = "SELECT * FROM notification WHERE user_id = :user_id AND type = :type ORDER BY id DESC", nativeQuery = true)
    Page<Notification> getNotification(@Param("user_id") int user_id, @Param("type") int type, Pageable pageable);

    @Query(value = "SELECT count(*) FROM notification WHERE user_id = :user_id AND read_at IS NULL", nativeQuery = true)
    Integer countNotificationUnRead(@Param("user_id") int user_id);

    @Modifying
    @Query(value = "UPDATE notification SET read_at = NULL WHERE id = :id", nativeQuery = true)
    void changeStatusUnRead(@Param("id") int id);

    @Modifying
    @Query(value = "UPDATE notification SET read_at = now() WHERE id = :id", nativeQuery = true)
    void changeStatusRead(@Param("id") int id);
}
