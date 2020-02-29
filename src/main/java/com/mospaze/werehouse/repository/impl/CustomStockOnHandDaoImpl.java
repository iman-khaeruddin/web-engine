package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.repository.CustomStockOnHandDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 03/12/2019
 * @see CustomStockOnHandDao
 */
@Repository
public class CustomStockOnHandDaoImpl implements CustomStockOnHandDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Deprecated
    public List<Object[]> findStockOnHand(String month, String date, String warehouse) {

        String query = "SELECT " +
                "  COUNT( soh.id ) AS jml, " +
                "  soh.description AS product_family, " +
                "  SUM( soh.qty_stock ) AS qty, " +
                "  soh.gedung AS warehouse  " +
                "FROM " +
                "  stock_on_hand soh  " +
                "WHERE 1=1 ";

        if (date != null)
            query = query.concat(" AND soh.date = '" + date + "' ");

        if (month != null)
            query = query.concat(" AND soh.month = '" + month + "' ");

        if (warehouse != null)
            query = query.concat(" AND soh.gedung =  '" + warehouse + "' ");

        query = query.concat("GROUP BY " +
                "  soh.description, " +
                "  soh.gedung");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }

    @Override
    public List<Object[]> findStockOnHandNew(String month, String date, int warehouse, int user_id, String start_date, String end_date) {

        String query = "SELECT " +
                "  COUNT( soh.id ) AS jml, " +
                "  soh.category AS product_family, " +
                "  SUM( soh.qty_stock ) AS qty, " +
                "  soh.warehouse  " +
                "FROM " +
                "  stock_on_hand_new soh  " +
                "WHERE 1=1 " +
                "AND soh.warehouse =  " + warehouse + " " +
                "AND soh.user_id =  " + user_id + " ";

        if (date != null)
            query = query.concat(" AND DATE_FORMAT(soh.date, '%Y-%m-%d') = '" + date +"'");

        if (month != null)
            query = query.concat(" AND DATE_FORMAT(soh.date, '%Y-%m') = '" + month +"'");

        if (start_date != null && end_date != null)
            query = query.concat(" AND (DATE_FORMAT(soh.date, '%Y-%m') >= '" + start_date + "' AND  DATE_FORMAT(soh.date, '%Y-%m') <= '" + end_date +"')");

        if (month == null && date == null && (start_date == null || end_date == null))
            query = query.concat(" AND soh.id = (SELECT MAX(id) FROM stock_on_hand_new) ");

        query = query.concat("GROUP BY " +
                "  soh.category, " +
                "  soh.warehouse");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }
}
