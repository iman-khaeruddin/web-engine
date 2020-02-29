package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface RolesDao extends CrudRepository<Roles, Integer> {

    @Query(value = "SELECT  " +
            "    r.* " +
            "FROM " +
            "    roles r " +
            "        JOIN " +
            "    model_has_roles mhr ON mhr.role_id = r.id " +
            "WHERE " +
            "    mhr.model_id = :user_id", nativeQuery = true)
    Roles getRoleByUserId(@Param("user_id") int user_id);
}
