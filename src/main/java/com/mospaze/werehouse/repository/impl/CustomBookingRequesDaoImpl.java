package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.projector.BookingRequest;
import com.mospaze.werehouse.repository.CustomBookingRequestDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public class CustomBookingRequesDaoImpl implements CustomBookingRequestDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookingRequest> getBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole modelHasRole, Integer page, Integer size) {
        String q = "SELECT  " +
                "    br.*, " +
                "    w.name AS warehouse_name, " +
                "    CASE " +
                "        WHEN br.status = 1 THEN 'OPEN' " +
                "        WHEN br.status = 2 THEN 'SELLER REVIEW' " +
                "        WHEN br.status = 3 THEN 'SELLER APPROVED' " +
                "        WHEN br.status = 4 THEN 'ADMIN REJECTED' " +
                "        WHEN br.status = 5 THEN 'QUOTATION REQUESTED' " +
                "        WHEN br.status = 6 THEN 'ADMIN APPROVED' " +
                "        WHEN br.status = 7 THEN 'SELLER REJECTED' " +
                "        WHEN br.status = 8 THEN 'ADMIN APPROVED REVIEW' " +
                "    END AS status_desc " +
                "FROM " +
                "    booking_request br " +
                "        JOIN " +
                "    warehouse w ON br.warehouse_id = w.id " +
                "WHERE 1=1 " ;

        if(status != null)
            q = q.concat(" AND br.status = " + status);

        if(warehouse_name != null)
            q = q.concat(" AND w.name LIKE CONCAT('%','"+warehouse_name+"','%') ");

        if(modelHasRole.getRole_id() == Constant.Role.BUYER.getRole())
            q = q.concat(" AND br.buyer_id = " + modelHasRole.getModel_id());

        if(modelHasRole.getRole_id() == Constant.Role.SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole())
            q = q.concat(" AND w.user_id = " + modelHasRole.getModel_id() + " AND br.status <> 1 ");

        q = q.concat(" ORDER BY br.id DESC");

        Query query = entityManager.createNativeQuery(q, BookingRequest.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    public BigDecimal getTotalPageBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole modelHasRole, Integer page, Integer size) {
        String q = "SELECT ceiling(count(*)/"+size+") as page FROM booking_request br JOIN warehouse w ON br.warehouse_id = w.id WHERE 1=1 " ;

        if(status != null)
            q = q.concat(" AND br.status = " + status);

        if(warehouse_name != null)
            q = q.concat(" AND w.name LIKE CONCAT('%','"+warehouse_name+"','%') ");

        if(modelHasRole.getRole_id() == Constant.Role.BUYER.getRole())
            q = q.concat(" AND br.buyer_id = " + modelHasRole.getModel_id());

        if(modelHasRole.getRole_id() == Constant.Role.SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole())
            q = q.concat(" AND w.user_id = " + modelHasRole.getModel_id() + " AND br.status <> 1 ");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    public BigInteger getTotalElementBookingRequestByStatus(Integer status, String warehouse_name, ModelHasRole modelHasRole, Integer page, Integer size) {
        String q = "SELECT count(br.id) as page FROM booking_request br JOIN warehouse w ON br.warehouse_id = w.id WHERE 1=1 " ;

        if(status != null)
            q = q.concat(" AND br.status = " + status);

        if(warehouse_name != null)
            q = q.concat(" AND w.name LIKE CONCAT('%','"+warehouse_name+"','%') ");

        if(modelHasRole.getRole_id() == Constant.Role.BUYER.getRole())
            q = q.concat(" AND br.buyer_id = " + modelHasRole.getModel_id());

        if(modelHasRole.getRole_id() == Constant.Role.SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole())
            q = q.concat(" AND w.user_id = " + modelHasRole.getModel_id() + " AND br.status <> 1 ");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

    @Override
    public BigInteger getCountBookingThisMonth(String code) {
        String q = "";

        if(code.equals(Constant.QuotationCode.SH.getCode()))
            q = "SELECT count(br.id) as page FROM booking_request br WHERE DATE_FORMAT(quotation_date, 'yyyyMM') = DATE_FORMAT(now(), 'yyyyMM') AND is_fulfillment_space = 1" ;

        if(code.equals(Constant.QuotationCode.SO.getCode()))
            q = "SELECT count(br.id) as page FROM booking_request br WHERE DATE_FORMAT(quotation_date, 'yyyyMM') = DATE_FORMAT(now(), 'yyyyMM') AND is_leasing_space = 1" ;

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }
}
