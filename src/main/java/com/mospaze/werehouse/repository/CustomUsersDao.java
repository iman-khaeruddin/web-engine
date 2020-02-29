package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.UserByRole;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface CustomUsersDao {
    /**
     * @param role
     * @param name_email
     * @param userRole
     * @param page
     * @param size
     * @return list of {@link UserByRole}
     */
    List<UserByRole> findByRole(Integer role, String name_email, ModelHasRole userRole, Integer page, Integer size);

    /**
     * @param role
     * @param name_email
     * @param userRole
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    BigDecimal getTotalPage(Integer role, String name_email, ModelHasRole userRole, Integer page, Integer size);

    /**
     * @param role_id
     * @param name_email
     * @param userRole
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    BigInteger getTotalElement(Integer role_id, String name_email, ModelHasRole userRole, Integer page, Integer size);
}
