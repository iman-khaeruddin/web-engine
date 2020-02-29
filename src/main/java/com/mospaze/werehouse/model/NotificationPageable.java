package com.mospaze.werehouse.model;

import com.mospaze.werehouse.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see java.io.Serializable
 */
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class NotificationPageable implements Serializable {
    private List<Notification> notification;
    private BigDecimal total_page;
    private BigDecimal total_element;
    private BigDecimal total_unread;

    @Override
    public String toString() {
        return "NotificationPageable{" +
                "notification=" + notification +
                ", total_page=" + total_page +
                ", total_element=" + total_element +
                ", total_unread=" + total_unread +
                '}';
    }
}
