package com.mospaze.werehouse.repository;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.entity.projector.WarehouseByStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface CustomWarehouseDao {
    /**
     * @param cities
     * @param size_min
     * @param size_max
     * @param feature
     * @param space_service
     * @param category
     * @param space
     * @param type
     * @param service
     * @param automation
     * @param unit
     * @param is_racking
     * @return list of {@link Warehouse}
     */
    @Deprecated
    List<Warehouse> findByFilter(Integer cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking);

    /**
     * @param cities
     * @param size_min
     * @param size_max
     * @param feature
     * @param space_service
     * @param category
     * @param space
     * @param type
     * @param service
     * @param automation
     * @param unit
     * @param is_racking
     * @param page
     * @param size
     * @param temperature
     * @param district
     * @param is_first_class
     * @param is_partner
     * @return
     */
    List<Warehouse> findByFilter(String cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature, String district, Integer is_first_class, Integer is_partner);

    /**
     * @param status
     * @param name_city
     * @param modelHasRole
     * @param page
     * @param size
     * @return list of {@link WarehouseByStatus}
     */
    List<WarehouseByStatus> findByStatus(Integer status, String name_city, ModelHasRole modelHasRole, Integer page, Integer size);

    /**
     * @param cities
     * @param size_min
     * @param size_max
     * @param feature
     * @param space_service
     * @param category
     * @param space
     * @param type
     * @param service
     * @param automation
     * @param unit
     * @param is_racking
     * @param page
     * @param size
     * @param temperature
     * @return {@link BigDecimal}
     */
    @Deprecated
    BigDecimal getTotalPageFindByFilter(Integer cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature);

    /**
     * @param status
     * @param name_city
     * @param modelHasRole
     * @param page
     * @param size
     * @return {@link BigDecimal}
     */
    BigDecimal getTotalPageFindByStatus(Integer status, String name_city, ModelHasRole modelHasRole, Integer page, Integer size);

    /**
     * @param cities
     * @param size_min
     * @param size_max
     * @param feature
     * @param space_service
     * @param category
     * @param space
     * @param type
     * @param service
     * @param automation
     * @param unit
     * @param is_racking
     * @param page
     * @param size
     * @param temperature
     * @param district
     * @param is_first_class
     * @param is_partner
     * @return {@link BigInteger}
     */
    BigInteger getTotalElementByFilter(String cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature, String district, Integer is_first_class, Integer is_partner);

    /**
     * @param status
     * @param name_city
     * @param role_login_user
     * @param page
     * @param size
     * @return {@link BigInteger}
     */
    BigInteger getTotalElementFindByStatus(Integer status, String name_city, ModelHasRole role_login_user, Integer page, Integer size);
}
