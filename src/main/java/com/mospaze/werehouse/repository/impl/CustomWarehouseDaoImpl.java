package com.mospaze.werehouse.repository.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.entity.projector.WarehouseByStatus;
import com.mospaze.werehouse.repository.CustomWarehouseDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public class CustomWarehouseDaoImpl implements CustomWarehouseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Deprecated
    public List<Warehouse> findByFilter(Integer cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking){
        /*CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Warehouse> warehouseCriteriaQuery = criteriaBuilder.createQuery(Warehouse.class);

        Root<Warehouse> warehouseRoot = warehouseCriteriaQuery.from(Warehouse.class);
        Predicate citiesPredicate = criteriaBuilder.equal(warehouseRoot.get("city_id"), cities);
        Predicate sizePredicate = criteriaBuilder.between(warehouseRoot.get("space"), size_min, size_max);
        Predicate deletedPredicate = criteriaBuilder.isNull(warehouseRoot.get("deleted_at"));

        warehouseCriteriaQuery.where(citiesPredicate, sizePredicate, deletedPredicate);

        TypedQuery<Warehouse> warehouseTypedQuery = entityManager.createQuery(warehouseCriteriaQuery);*/

        String q = "SELECT DISTINCT w.* FROM warehouse w";

        if(feature != null)
            q = q.concat(" LEFT JOIN warehouse_features wf ON w.id = wf.warehouse_id");
        if(space_service != null)
            q = q.concat(" LEFT JOIN warehouse_space_service wss ON w.id = wss.warehouse_id");
        if(category != null)
            q = q.concat(" LEFT JOIN warehouse_category wc ON w.id = wc.warehouse_id");
        if(space != null)
            q = q.concat(" LEFT JOIN warehouse_space wsp ON w.id = wsp.warehouse_id");
        if(type != null)
            q = q.concat(" LEFT JOIN warehouse_types wt ON w.id = wt.warehouse_id");
        if(service != null)
            q = q.concat(" LEFT JOIN warehouse_services ws ON w.id = ws.warehouse_id");
        if(automation != null)
            q = q.concat(" LEFT JOIN warehouse_automation wa ON w.id = wa.warehouse_id");

        q = q.concat(" WHERE w.deleted_at IS NULL AND w.status_verification = 4 AND status = 1 ");
//        q = q.concat(" AND status_available = 1 ");

        if(cities != null)
            q = q.concat(" AND w.city_id = "+ cities);
        if(size_min != null && size_max != null && unit != null)
            if(unit == 2)
                q = q.concat(" AND w.space BETWEEN "+size_min+" AND "+size_max);
            else if(unit == 1)
                q = q.concat(" AND w.palletnum BETWEEN "+size_min+" AND "+size_max);
        if(is_racking != null)
            q = q.concat(" AND w.has_rack_available = "+ is_racking);
        if(feature != null)
            q = q.concat(" AND wf.features_id IN ("+feature+")");
        if(space_service != null)
            q = q.concat(" AND wss.space_service_id IN ("+space_service+")");
        if(category != null)
            q = q.concat(" AND wc.category_id IN ("+category+")");
        if(space != null)
            q = q.concat(" AND wsp.space_id IN ("+space+")");
        if(type != null)
            q = q.concat(" AND wt.types_id IN ("+type+")");
        if(service != null)
            q = q.concat(" AND ws.services_id IN ("+service+")");
        if(automation != null)
            q = q.concat(" AND wa.automation_id IN ("+automation+")");

        Query query = entityManager.createNativeQuery(q, Warehouse.class);

//        List<Warehouse> warehouses = entityManager.createNamedQuery("getAllWarehouseByFilter", Warehouse.class).getResultList();
        return  query.getResultList();
    }

    @Override
    public List<Warehouse> findByFilter(String cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature, String district, Integer is_first_class, Integer is_partner) {

        String q = "SELECT DISTINCT w.* FROM warehouse w";

        if(feature != null)
            q = q.concat(" LEFT JOIN warehouse_features wf ON w.id = wf.warehouse_id");
        if(space_service != null)
            q = q.concat(" LEFT JOIN warehouse_space_service wss ON w.id = wss.warehouse_id");
        if(category != null)
            q = q.concat(" LEFT JOIN warehouse_category wc ON w.id = wc.warehouse_id");
        if(space != null)
            q = q.concat(" LEFT JOIN warehouse_space wsp ON w.id = wsp.warehouse_id");
        if(type != null)
            q = q.concat(" LEFT JOIN warehouse_types wt ON w.id = wt.warehouse_id");
        if(service != null)
            q = q.concat(" LEFT JOIN warehouse_services ws ON w.id = ws.warehouse_id");
        if(automation != null)
            q = q.concat(" LEFT JOIN warehouse_automation wa ON w.id = wa.warehouse_id");
        if(temperature != null)
            q = q.concat(" LEFT JOIN warehouse_temperature wtemp ON w.id = wtemp.warehouse_id");

        q = q.concat(" WHERE w.deleted_at IS NULL AND w.status_verification = 4 AND status = 1 ");
//        q = q.concat(" AND status_available = 1 ");

        if(cities != null && district != null)
            q = q.concat(" AND (w.city_id IN (" + cities + ") OR w.district_id IN (" + district + ")) ");
        if(size_min != null && size_max != null && unit != null)
            if(unit == 2) //sqm
                q = q.concat(" AND w.space BETWEEN "+size_min+" AND "+size_max);
            else if(unit == 1)  //pallet
                q = q.concat(" AND w.palletnum BETWEEN "+size_min+" AND "+size_max);
        if(is_racking != null)
            q = q.concat(" AND w.has_rack_available = "+ is_racking);
        if(feature != null)
            q = q.concat(" AND wf.features_id IN ("+feature+")");
        if(space_service != null)
            q = q.concat(" AND wss.space_service_id IN ("+space_service+")");
        if(category != null)
            q = q.concat(" AND wc.category_id IN ("+category+")");
        if(space != null)
            q = q.concat(" AND wsp.space_id IN ("+space+")");
        if(type != null)
            q = q.concat(" AND wt.types_id IN ("+type+")");
        if(service != null)
            q = q.concat(" AND ws.services_id IN ("+service+")");
        if(automation != null)
            q = q.concat(" AND wa.automation_id IN ("+automation+")");
        if(temperature != null)
            q = q.concat(" AND wtemp.temperature_id IN ("+temperature+")");
        if(district != null && cities == null)
            q = q.concat(" AND w.district_id IN (" + district + ")");
        if(district == null && cities != null)
            q = q.concat(" AND w.city_id IN ("+ cities + ")");
        if(is_first_class != null)
            q = q.concat(" AND w.is_first_class_warehouse = "+ is_first_class);
        if(is_partner != null)
            q = q.concat(" AND w.is_partner = "+ is_partner);

        q = q.concat(" ORDER BY w.is_partner DESC");

        Query query = entityManager.createNativeQuery(q, Warehouse.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    public List<WarehouseByStatus> findByStatus(Integer status, String name_city, ModelHasRole modelHasRole, Integer page, Integer size) {

        String q = String.format("SELECT  " +
                "    w.id, " +
                "    w.name, " +
                "    w.is_partner, " +
                "    w.is_first_class_warehouse, " +
                "    CONCAT(u.first_name, ' ', u.last_name) AS owner, " +
                "    mlc.name AS city, " +
                "    mls.name AS state, " +
                "    mlco.name AS country, " +
                "    status_verification, " +
                "    w.status, " +
                "    w.visit_count, " +
                "    w.created_at," +
                "    CASE " +
                "        WHEN w.status_verification = 1 THEN 'Pending Verification' " +
                "        WHEN w.status_verification = 2 THEN 'Information Verification' " +
                "        WHEN w.status_verification = 3 THEN 'Warehouse Inspection' " +
                "        WHEN w.status_verification = 4 THEN 'Approved' " +
                "        WHEN w.status_verification = 5 THEN 'Rejected' " +
                "    END AS status_verification_desc, " +
                "    CASE " +
                "        WHEN w.status = 0 THEN 'Inactive' " +
                "        WHEN w.status = 1 THEN 'Active' " +
                "    END AS status_desc " +
                "FROM " +
                "    warehouse w " +
                "        JOIN " +
                "    users u ON w.user_id = u.id " +
                "        JOIN " +
                "    master_location_cities mlc ON w.city_id = mlc.id " +
                "        JOIN " +
                "    master_location_states mls ON mls.id = mlc.state_id " +
                "        JOIN " +
                "    master_location_countries mlco ON mlco.id = mls.country_id " +
                "WHERE " +
                "    w.deleted_at IS NULL ");

        if(status != null)
            q = q.concat(" AND  w.status_verification = " + status );

        if(name_city != null)
            q = q.concat(" AND  (w.name LIKE  CONCAT('%','"+name_city+"','%') OR mlc.name LIKE  CONCAT('%','"+name_city+"','%'))");

        if(modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND  w.user_id = " + modelHasRole.getModel_id() );

        Query query = entityManager.createNativeQuery(q, WarehouseByStatus.class);

        return query.setFirstResult(page*size).setMaxResults(size).getResultList();
    }

    @Override
    @Deprecated
    public BigDecimal getTotalPageFindByFilter(Integer cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature) {

        String q = "SELECT ceiling(count(*)/"+size+") as page FROM warehouse w";

        if(feature != null)
            q = q.concat(" LEFT JOIN warehouse_features wf ON w.id = wf.warehouse_id");
        if(space_service != null)
            q = q.concat(" LEFT JOIN warehouse_space_service wss ON w.id = wss.warehouse_id");
        if(category != null)
            q = q.concat(" LEFT JOIN warehouse_category wc ON w.id = wc.warehouse_id");
        if(space != null)
            q = q.concat(" LEFT JOIN warehouse_space wsp ON w.id = wsp.warehouse_id");
        if(type != null)
            q = q.concat(" LEFT JOIN warehouse_types wt ON w.id = wt.warehouse_id");
        if(service != null)
            q = q.concat(" LEFT JOIN warehouse_services ws ON w.id = ws.warehouse_id");
        if(automation != null)
            q = q.concat(" LEFT JOIN warehouse_automation wa ON w.id = wa.warehouse_id");
        if(temperature != null)
            q = q.concat(" LEFT JOIN warehouse_temperature wtemp ON w.id = wtemp.warehouse_id");

        q = q.concat(" WHERE w.deleted_at IS NULL AND w.status_verification = 4 AND status = 1 ");
//        q = q.concat(" AND status_available = 1 ");

        if(cities != null)
            q = q.concat(" AND w.city_id = "+ cities);
        if(size_min != null && size_max != null && unit != null)
            if(unit == 2) //sqm
                q = q.concat(" AND w.space BETWEEN "+size_min+" AND "+size_max);
            else if(unit == 1)  //pallet
                q = q.concat(" AND w.palletnum BETWEEN "+size_min+" AND "+size_max);
        if(is_racking != null)
            q = q.concat(" AND w.has_rack_available = "+ is_racking);
        if(feature != null)
            q = q.concat(" AND wf.features_id IN ("+feature+")");
        if(space_service != null)
            q = q.concat(" AND wss.space_service_id IN ("+space_service+")");
        if(category != null)
            q = q.concat(" AND wc.category_id IN ("+category+")");
        if(space != null)
            q = q.concat(" AND wsp.space_id IN ("+space+")");
        if(type != null)
            q = q.concat(" AND wt.types_id IN ("+type+")");
        if(service != null)
            q = q.concat(" AND ws.services_id IN ("+service+")");
        if(automation != null)
            q = q.concat(" AND wa.automation_id IN ("+automation+")");
        if(temperature != null)
            q = q.concat(" AND wtemp.temperature_id IN ("+temperature+")");

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    public BigDecimal getTotalPageFindByStatus(Integer status, String name_city, ModelHasRole modelHasRole, Integer page, Integer size) {

        String q = "SELECT ceiling(count(*)/"+size+") as page " +
                "FROM " +
                "    warehouse w " +
                "        JOIN " +
                "    users u ON w.user_id = u.id " +
                "        JOIN " +
                "    master_location_cities mlc ON w.city_id = mlc.id " +
                "        JOIN " +
                "    master_location_states mls ON mls.id = mlc.state_id " +
                "        JOIN " +
                "    master_location_countries mlco ON mlco.id = mls.country_id " +
                "WHERE w.deleted_at IS NULL " ;

        if(status != null)
            q = q.concat(" AND  w.status_verification = " + status );

        if(name_city != null)
            q = q.concat(" AND  (w.name LIKE  CONCAT('%','"+name_city+"','%') OR mlc.name LIKE  CONCAT('%','"+name_city+"','%'))");

        if(modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND  w.user_id = " + modelHasRole.getModel_id() );

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigDecimal) object;
    }

    @Override
    public BigInteger getTotalElementByFilter(String cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature, String district, Integer is_first_class, Integer is_partner) {

        String q = "SELECT count(DISTINCT w.id) as element FROM warehouse w";

        if(feature != null)
            q = q.concat(" LEFT JOIN warehouse_features wf ON w.id = wf.warehouse_id");
        if(space_service != null)
            q = q.concat(" LEFT JOIN warehouse_space_service wss ON w.id = wss.warehouse_id");
        if(category != null)
            q = q.concat(" LEFT JOIN warehouse_category wc ON w.id = wc.warehouse_id");
        if(space != null)
            q = q.concat(" LEFT JOIN warehouse_space wsp ON w.id = wsp.warehouse_id");
        if(type != null)
            q = q.concat(" LEFT JOIN warehouse_types wt ON w.id = wt.warehouse_id");
        if(service != null)
            q = q.concat(" LEFT JOIN warehouse_services ws ON w.id = ws.warehouse_id");
        if(automation != null)
            q = q.concat(" LEFT JOIN warehouse_automation wa ON w.id = wa.warehouse_id");
        if(temperature != null)
            q = q.concat(" LEFT JOIN warehouse_temperature wtemp ON w.id = wtemp.warehouse_id");

        q = q.concat(" WHERE w.deleted_at IS NULL AND w.status_verification = 4 AND status = 1 ");
//        q = q.concat(" AND status_available = 1 ");

        if(cities != null && district != null)
            q = q.concat(" AND (w.city_id IN (" + cities + ") OR w.district_id IN (" + district + ")) ");
        if(size_min != null && size_max != null && unit != null)
            if(unit == 2) //sqm
                q = q.concat(" AND w.space BETWEEN "+size_min+" AND "+size_max);
            else if(unit == 1)  //pallet
                q = q.concat(" AND w.palletnum BETWEEN "+size_min+" AND "+size_max);
        if(is_racking != null)
            q = q.concat(" AND w.has_rack_available = "+ is_racking);
        if(feature != null)
            q = q.concat(" AND wf.features_id IN ("+feature+")");
        if(space_service != null)
            q = q.concat(" AND wss.space_service_id IN ("+space_service+")");
        if(category != null)
            q = q.concat(" AND wc.category_id IN ("+category+")");
        if(space != null)
            q = q.concat(" AND wsp.space_id IN ("+space+")");
        if(type != null)
            q = q.concat(" AND wt.types_id IN ("+type+")");
        if(service != null)
            q = q.concat(" AND ws.services_id IN ("+service+")");
        if(automation != null)
            q = q.concat(" AND wa.automation_id IN ("+automation+")");
        if(temperature != null)
            q = q.concat(" AND wtemp.temperature_id IN ("+temperature+")");
        if(district != null && cities == null)
            q = q.concat(" AND w.district_id IN (" + district + ")");
        if(district == null && cities != null)
            q = q.concat(" AND w.city_id IN ("+ cities + ")");
        if(is_first_class != null)
            q = q.concat(" AND w.is_first_class_warehouse = "+ is_first_class);
        if(is_partner != null)
            q = q.concat(" AND w.is_partner = "+ is_partner);

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

    @Override
    public BigInteger getTotalElementFindByStatus(Integer status, String name_city, ModelHasRole modelHasRole, Integer page, Integer size) {

        String q = "SELECT count(w.id) as page " +
                "FROM " +
                "    warehouse w " +
                "        JOIN " +
                "    users u ON w.user_id = u.id " +
                "        JOIN " +
                "    master_location_cities mlc ON w.city_id = mlc.id " +
                "        JOIN " +
                "    master_location_states mls ON mls.id = mlc.state_id " +
                "        JOIN " +
                "    master_location_countries mlco ON mlco.id = mls.country_id " +
                "WHERE w.deleted_at IS NULL " ;

        if(status != null)
            q = q.concat(" AND  w.status_verification = " + status );

        if(name_city != null)
            q = q.concat(" AND  (w.name LIKE  CONCAT('%','"+name_city+"','%') OR mlc.name LIKE  CONCAT('%','"+name_city+"','%'))");

        if(modelHasRole.getRole_id() == Constant.Role.SUB_SELLER.getRole() || modelHasRole.getRole_id() == Constant.Role.SELLER.getRole())
            q = q.concat(" AND  w.user_id = " + modelHasRole.getModel_id() );

        Query query = entityManager.createNativeQuery(q);

        Object object = query.getSingleResult();

        return (BigInteger) object;
    }

}
