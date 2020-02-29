package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.ModelHasRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface ModelHasRoleDao extends CrudRepository<ModelHasRole, Integer> {
    @Query(value = "SELECT role_id FROM model_has_roles WHERE model_id = :user_id", nativeQuery = true)
    Integer findRoleByUserId(@Param("user_id") int user_id);
}
