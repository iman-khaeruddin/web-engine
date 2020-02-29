package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.UserByRole;
import com.mospaze.werehouse.repository.CustomUsersDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public class CustomUsersDaoImpl implements CustomUsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserByRole> findByRole(Integer role, String name_email, ModelHasRole role_login_user, Integer page, Integer size) {
        String q = String.format("SELECT   " +
                "    r.name AS user_type,  " +
                "    u.id,  " +
                "    u.email,  " +
                "    u.verified_phone,  " +
                "    u.status,  " +
                "    CONCAT(u.first_name, ' ', u.last_name) AS name  " +
                "FROM  " +
                "    roles r  " +
                "        JOIN  " +
                "    model_has_roles mrh ON mrh.role_id = r.id  " +
                "        JOIN  " +
                "    users u ON mrh.model_id = u.id  " +
                "WHERE u.deleted_at IS NULL ") ;

        if(role != null)
            q = q.concat(" AND   r.id = " + role);

        if(name_email != null)
            q = q.concat(" AND   (u.first_name LIKE  CONCAT('%','"+name_email+"','%') OR u.last_name LIKE  CONCAT('%','"+name_email+"','%') OR u.email LIKE  CONCAT('%','"+name_email+"','%') )");

        if(role_login_user.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND   (mrh.super_user = " + role_login_user.getModel_id() +")");

        if(role_login_user.getRole_id() == Constant.Role.ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole() + " AND r.id <> " + Constant.Role.ADMIN.getRole());

        if(role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole());

        Query query = entityManager.createNativeQuery(q, UserByRole.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    public BigDecimal getTotalPage(Integer role, String name_email, ModelHasRole role_login_user, Integer page, Integer size) {
        String q = "SELECT   " +
                "    ceiling(count(*)/"+size+") as page  " +
                "FROM  " +
                "    roles r  " +
                "        JOIN  " +
                "    model_has_roles mrh ON mrh.role_id = r.id  " +
                "        JOIN  " +
                "    users u ON mrh.model_id = u.id  " +
                "WHERE u.deleted_at IS NULL " ;

        if(role != null)
            q = q.concat(" AND   r.id = " + role);

        if(name_email != null)
            q = q.concat(" AND   (u.first_name LIKE  CONCAT('%','"+name_email+"','%') OR u.last_name LIKE  CONCAT('%','"+name_email+"','%') OR u.email LIKE  CONCAT('%','"+name_email+"','%') )");

        if(role_login_user.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND   (mrh.super_user = " + role_login_user.getModel_id() +")");

        if(role_login_user.getRole_id() == Constant.Role.ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole() + " AND r.id <> " + Constant.Role.ADMIN.getRole());

        if(role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole());

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    public BigInteger getTotalElement(Integer role, String name_email, ModelHasRole role_login_user, Integer page, Integer size) {
        String q = "SELECT   " +
                "    count(r.id) as page  " +
                "FROM  " +
                "    roles r  " +
                "        JOIN  " +
                "    model_has_roles mrh ON mrh.role_id = r.id  " +
                "        JOIN  " +
                "    users u ON mrh.model_id = u.id  " +
                "WHERE u.deleted_at IS NULL " ;

        if(role != null)
            q = q.concat(" AND   r.id = " + role);

        if(name_email != null)
            q = q.concat(" AND   (u.first_name LIKE  CONCAT('%','"+name_email+"','%') OR u.last_name LIKE  CONCAT('%','"+name_email+"','%') OR u.email LIKE  CONCAT('%','"+name_email+"','%') )");

        if(role_login_user.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND   (mrh.super_user = " + role_login_user.getModel_id() +")");

        if(role_login_user.getRole_id() == Constant.Role.ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole() + " AND r.id <> " + Constant.Role.ADMIN.getRole());

        if(role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
            q = q.concat(" AND   r.id <> " + Constant.Role.SUPER_ADMIN.getRole());

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

}
