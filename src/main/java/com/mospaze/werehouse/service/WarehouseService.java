package com.mospaze.werehouse.service;

import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.*;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface WarehouseService {

	/**
	 * @return list of {@link DetailWarehouse}
	 */
	@Deprecated
	List<DetailWarehouse> GetAllDataWarehouse();

	/**
	 * @param page
	 * @param size
	 * @return all entities {@link DetailWarehouse} with pageable
	 */
	WarehousePageable getAllDataWarehousePageable(Integer page, Integer size);

	/**
	 * @param id
	 * @return {@link GeneralResponse}
	 */
	GeneralResponse getDetailWarehouse(int id);

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
	 * @return list of {@link DetailWarehouse}
	 */
	@Deprecated
	List<DetailWarehouse> GetAllDataWarehouse(Integer cities,
                                                     Integer size_min,
                                                     Integer size_max,
                                                     String feature,
                                                     String space_service,
                                                     String category,
                                                     String space,
                                                     String type,
                                                     String service,
                                                     String automation,
                                                     Integer unit,
													 Integer is_racking);

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
     * @return all entities {@link DetailWarehouse} with pageable
	 */
	WarehousePageable getAllDataByFilterWarehousePageable(String cities,
                                                          Integer size_min,
                                                          Integer size_max,
                                                          String feature,
                                                          String space_service,
                                                          String category,
                                                          String space,
                                                          String type,
                                                          String service,
                                                          String automation,
                                                          Integer unit,
                                                          Integer is_racking,
                                                          Integer page,
                                                          Integer size,
                                                          String temperature,
                                                          String district, Integer is_first_class, Integer is_partner);

	/**
	 * @param status
	 * @param name_city
	 * @param modelHasRole
	 * @param page
	 * @param size
	 * @return all entities {@link DetailWarehouse} with pageable
	 */
	WarehousePageable getListWarehouseByStatus(Integer status,
                                               String name_city,
                                               ModelHasRole modelHasRole,
											   Integer page,
                                               Integer size);

	/**
	 * @param warehouseManagementRequest
	 * @param username
	 * @return {@link String}
	 */
	String updateWarehouse(WarehouseManagementRequest warehouseManagementRequest, String username);

	/**
	 * @param id
	 * @param username
	 * @return {@link String}
	 */
    String deleteWarehouse(int id, String username);

	/**
	 * @param status_verification
	 * @param display_status
	 * @param id
	 * @param username
	 * @param is_send_notification
	 * @param notes
	 * @return {@link String}
	 */
    String changeStatusVerificationWarehouse(Integer status_verification, int display_status, int id, String username, int is_send_notification, String notes);

	/**
	 * @param id
	 * @param username
	 * @return {@link GeneralResponse}
	 */
    GeneralResponse getDetailWarehouseById(int id, String username);

	/**
	 * @param warehouseManagementRequest
	 * @param role_login_user
	 * @return {@link Warehouse}
	 */
    Warehouse insertWarehouse(WarehouseManagementRequest warehouseManagementRequest, Optional<ModelHasRole> role_login_user);

	/**
	 * @param role_login_user
	 * @return {@link NotificationResponse}
	 */
	@Deprecated
    NotificationResponse getNotificationWarehouse(ModelHasRole role_login_user);

	/**
	 * @param status
	 * @param id
	 * @param jwt
	 * @return {@link String}
	 */
    String changeWarehousePartner(int status, int id, JWT jwt);

	/**
	 * @return {@link GeneralResponse}
	 */
	GeneralResponse getAllWarehouseFirstClass();

	/**
	 * @param status
	 * @param id
	 * @param jwt
	 * @return {@link String}
	 */
    String changeWarehouseFirstClass(int status, int id, JWT jwt);
}
