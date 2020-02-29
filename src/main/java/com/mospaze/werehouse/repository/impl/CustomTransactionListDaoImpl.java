package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.entity.TransactionList;
import com.mospaze.werehouse.entity.TransactionListNew;
import com.mospaze.werehouse.repository.CustomTransactionListDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 04/12/2019
 * @see CustomTransactionListDao
 */
@Repository
public class CustomTransactionListDaoImpl implements CustomTransactionListDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Deprecated
    public List<TransactionList> findJobList(String warehouse, String status, Integer page, Integer size) {
        String q = String.format("SELECT * FROM transaction_list WHERE 1=1 ") ;

        if(warehouse != null)
            q = q.concat(" AND warehouse = '" + warehouse + "'");

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");


        q = q.concat(" ORDER BY id DESC");

        Query query = entityManager.createNativeQuery(q, TransactionList.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    @Deprecated
    public BigDecimal getTotalPage(String warehouse, String status, Integer page, Integer size) {
        String q = String.format("SELECT ceiling(count(*)/"+size+") as page FROM transaction_list WHERE 1=1 ") ;

        if(warehouse != null)
            q = q.concat(" AND warehouse = '" + warehouse + "'");

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    @Deprecated
    public BigInteger getTotalElement(String warehouse, String status, Integer page, Integer size) {
        String q = String.format("SELECT count(*) FROM transaction_list WHERE 1=1 ") ;

        if(warehouse != null)
            q = q.concat(" AND warehouse = '" + warehouse + "'");

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

    @Override
    public List<TransactionListNew> findJobListNew(int user_id, int warehouse, String status, int page, int size) {
        String q = String.format("SELECT * FROM transaction_list_new WHERE warehouse = " + warehouse + " " +
                "AND user_id = " + user_id + " ") ;

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");


        q = q.concat(" ORDER BY id DESC");

        Query query = entityManager.createNativeQuery(q, TransactionListNew.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    public BigDecimal getTotalPageNew(int user_id, int warehouse, String status, int page, int size) {
        String q = String.format("SELECT ceiling(count(*)/"+size+") as page FROM transaction_list_new WHERE warehouse = " + warehouse + " " +
                "AND user_id = " + user_id + " ") ;

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    public BigInteger getTotalElementNew(int user_id, int warehouse, String status, int page, int size) {
        String q = String.format("SELECT count(*) as page FROM transaction_list_new WHERE warehouse = " + warehouse + " " +
                "AND user_id = " + user_id + " ") ;

        if(status != null)
            q = q.concat(" AND status = '" + status + "'");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }
}
