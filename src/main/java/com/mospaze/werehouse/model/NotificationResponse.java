package com.mospaze.werehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.Notification;
import lombok.*;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private int total_notification;
    private List<Notification> notifications;
}
