package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.repository.CustomTokenDao;
import com.mospaze.werehouse.repository.TokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;

@Repository
public class CustomTokenDaoImpl implements CustomTokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger isTimeOut(String token, String username, String token_type) {
        String q = String.format("SELECT TIME_TO_SEC(TIMEDIFF(now(), timestamp)) as diff FROM token where token = :token AND username = :username AND flag_active = 1 AND type = :type");

        Query query = entityManager.createNativeQuery(q);
        query.setParameter("token", token);
        query.setParameter("username", username);
        query.setParameter("type", token_type);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

    @Override
    public BigInteger isAlreadyExist(String token) {
        String q = "SELECT count(id) as diff FROM token where token = :token AND flag_active = 1";

        Query query = entityManager.createNativeQuery(q);
        query.setParameter("token", token);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }
}
