package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.Notification;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.NotificationPageable;
import com.mospaze.werehouse.repository.NotificationDao;
import com.mospaze.werehouse.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.NotificationService
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public Notification insertNotification(Notification notification) {
        return notificationDao.save(notification);
    }

    @Override
    public NotificationPageable getNotification(Integer user_id, Integer type, Integer page, Integer size) {
        NotificationPageable notificationPageable = new NotificationPageable();

        Pageable pageable = PageRequest.of(page, size);

        Page<Notification> notifications = notificationDao.getNotification(user_id, type, pageable);

        notificationPageable.setNotification(notifications.getContent());
        notificationPageable.setTotal_page(BigDecimal.valueOf(notifications.getTotalPages()));
        notificationPageable.setTotal_element(BigDecimal.valueOf(notifications.getTotalElements()));
        notificationPageable.setTotal_unread(BigDecimal.valueOf(notificationDao.countNotificationUnRead(user_id)));

        return notificationPageable;
    }

    @Override
    @Transactional
    public String changeStatusUnRead(int id, JWT jwt) {
        Optional<Notification> notification = notificationDao.findById(id);

        if(notification.isPresent()){
            if(notification.get().getUser_id() != jwt.getUser_id())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            notificationDao.changeStatusUnRead(id);
            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }

    @Override
    @Transactional
    public String changeStatusRead(int id, JWT jwt) {
        Optional<Notification> notification = notificationDao.findById(id);

        if(notification.isPresent()){
            if(notification.get().getUser_id() != jwt.getUser_id())
                return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();

            notificationDao.changeStatusRead(id);
            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }
}
