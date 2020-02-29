package com.mospaze.werehouse.entity.projector;

import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
public interface User {
    String getUser_type();
    int getId();
    Integer getRole_id();
    String getOwner_name();
    String getEmail();
    Date getFlag_change_password();
}
