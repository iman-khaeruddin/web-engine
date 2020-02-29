package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.Permissions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.CrudRepository
 */
public interface PermissionDao extends CrudRepository<Permissions, Integer> {
    @Query(value = "SELECT  " +
            "    p.name " +
            "FROM " +
            "    permissions p " +
            "        JOIN " +
            "    role_has_permissions rhp ON p.id = rhp.permission_id " +
            "        JOIN " +
            "    roles r ON rhp.role_id = r.id " +
            "        JOIN " +
            "    model_has_roles mhr ON r.id = mhr.role_id" +
            "        JOIN " +
            "    users u ON mhr.model_id = u.id " +
            "WHERE " +
            "    u.phone = :username", nativeQuery = true)
    List<String> getPermissionByUsername(@Param("username") String username);
}
