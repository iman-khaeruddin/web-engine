package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.repository.CustomDataTrackerDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 * @see com.mospaze.werehouse.repository.CustomDataTrackerDao
 */
@Repository
public class CustomDataTrackerDaoImpl implements CustomDataTrackerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> findDataTrackerByOs(String month, String date) {
        String query = "SELECT " +
                "  COUNT( dt.id ) AS jml, " +
                "  dt.os AS os " +
                "FROM " +
                "  data_tracker dt  " +
                "WHERE 1=1 ";

        if (date != null && month == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m-%d') = '" + date + "' ");

        if (month != null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month != null && date != null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month == null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') ");

        query = query.concat("GROUP BY " +
                "  dt.os ");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }

    @Override
    public List<Object[]> findDataTrackerByBrowser(String month, String date) {
        String query = "SELECT " +
                "  COUNT( dt.id ) AS jml, " +
                "  dt.browser AS browser " +
                "FROM " +
                "  data_tracker dt  " +
                "WHERE 1=1 ";

        if (date != null && month == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m-%d') = '" + date + "' ");

        if (month != null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month != null && date != null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month == null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') ");

        query = query.concat("GROUP BY " +
                "  dt.browser ");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }

    @Override
    public List<Object[]> findDataTrackerByIpAddress(String month, String date) {
        String query = "SELECT " +
                "  COUNT( dt.id ) AS jml, " +
                "  dt.ip_address AS ip_address " +
                "FROM " +
                "  data_tracker dt  " +
                "WHERE 1=1 ";

        if (date != null && month == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m-%d') = '" + date + "' ");

        if (month != null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month != null && date != null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month == null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') ");

        query = query.concat("GROUP BY " +
                "  dt.ip_address ");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }

    @Override
    public List<Object[]> findDataTrackerByCity(String month, String date) {
        String query = "SELECT " +
                "  COUNT( dt.id ) AS jml, " +
                "  dt.city AS city " +
                "FROM " +
                "  data_tracker dt  " +
                "WHERE 1=1 ";

        if (date != null && month == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m-%d') = '" + date + "' ");

        if (month != null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month != null && date != null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month == null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') ");

        query = query.concat("GROUP BY " +
                "  dt.city ");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }

    @Override
    public List<Object[]> findDataTrackerByDistrict(String month, String date) {
        String query = "SELECT " +
                "  COUNT( dt.id ) AS jml, " +
                "  dt.district AS district " +
                "FROM " +
                "  data_tracker dt  " +
                "WHERE 1=1 ";

        if (date != null && month == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m-%d') = '" + date + "' ");

        if (month != null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month != null && date != null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = '" + month + "' ");

        if (month == null && date == null)
            query = query.concat(" AND DATE_FORMAT(dt.request_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m') ");

        query = query.concat("GROUP BY " +
                "  dt.district ");

        Query q = entityManager.createNativeQuery(query);

        return q.getResultList();
    }
}
