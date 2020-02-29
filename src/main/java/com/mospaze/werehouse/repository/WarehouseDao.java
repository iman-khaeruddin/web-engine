package com.mospaze.werehouse.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mospaze.werehouse.entity.Warehouse;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see org.springframework.data.repository.PagingAndSortingRepository
 */
public interface WarehouseDao extends PagingAndSortingRepository<Warehouse, Integer> {

	@Query(value = "SELECT w.* FROM warehouse w WHERE status_verification = 4 AND status = 1 AND deleted_at IS NULL ", nativeQuery = true)
	List<Warehouse> GetDataAllWarehouse();

	@Query(value = "SELECT w.* FROM warehouse w WHERE status_verification = 4 AND status = 1 AND deleted_at IS NULL ", nativeQuery = true)
	List<Warehouse> GetDataAllWarehouse(Pageable pageable);

	@Query(value = "SELECT * FROM warehouse WHERE status_verification = 4 AND status = 1 AND deleted_at IS NULL ", nativeQuery = true)
	Page<Warehouse> GetDataAllWarehousePageable(Pageable pageable);

	@Query(value = "select ceiling(count(*)/:size) as page from warehouse where status_verification = 4 and status = 1 AND deleted_at IS NULL", nativeQuery = true)
	int getTotalPage(@Param("size") int size);

	@Query(value = "SELECT  " +
			"    ifnull(MAX(palletnum), 0) AS max_size " +
			"FROM " +
			"    warehouse w " +
			"        JOIN " +
			"    warehouse_temperature wt ON w.id = wt.warehouse_id " +
			"WHERE " +
			"    w.city_id = :id_cities " +
			"        AND wt.temperature_id = :temperature " +
			"        AND w.status_verification = 4 " +
			"        AND w.status = 1" +
			"        AND w.status_available = 1" +
			"        AND w.deleted_at IS NULL", nativeQuery = true)
	int getMaxSizeByPallet(@Param("id_cities") Integer id_cities, @Param("temperature") Integer temperature);

	@Query(value = "SELECT  " +
			"    ifnull(MAX(space), 0) AS max_size " +
			"FROM " +
			"    warehouse w " +
			"        JOIN " +
			"    warehouse_temperature wt ON w.id = wt.warehouse_id " +
			"WHERE " +
			"    w.city_id = :id_cities " +
			"        AND wt.temperature_id = :temperature " +
			"        AND w.status_verification = 4 " +
			"        AND w.status = 1" +
			"        AND w.status_available = 1" +
			"        AND w.deleted_at IS NULL", nativeQuery = true)
	int getMaxSizeBySQM(@Param("id_cities") Integer id_cities, @Param("temperature") Integer temperature);

	@Query(value = "SELECT  " +
			"    ifnull(AVG(rack_rent_price), 0) AS average " +
			"FROM " +
			"    warehouse w " +
			"        JOIN " +
			"    warehouse_temperature wt ON w.id = wt.warehouse_id " +
			"WHERE " +
			"    w.city_id = :id_cities " +
			"        AND wt.temperature_id = :temperature " +
			"        AND w.status_verification = 4 " +
			"        AND w.status = 1" +
			"        AND w.status_available = 1" +
			"        AND w.rack_rent_price > 0" +
			"        AND w.deleted_at IS NULL", nativeQuery = true)
	double getAverageByPallet(@Param("id_cities") Integer id_cities, @Param("temperature") Integer temperature);

	@Query(value = "SELECT  " +
			"    ifnull(AVG(stack_rent_price), 0) AS average " +
			"FROM " +
			"    warehouse w " +
			"        JOIN " +
			"    warehouse_temperature wt ON w.id = wt.warehouse_id " +
			"WHERE " +
			"    w.city_id = :id_cities " +
			"        AND wt.temperature_id = :temperature " +
			"        AND w.status_verification = 4 " +
			"        AND w.status = 1" +
			"        AND w.status_available = 1" +
			"        AND w.stack_rent_price > 0" +
			"        AND w.deleted_at IS NULL", nativeQuery = true)
	double getAverageBySQM(@Param("id_cities") Integer id_cities, @Param("temperature") Integer temperature);

	@Modifying
	@Query(value = "UPDATE warehouse SET " +
			"user_id = :user_id, " +
			"name = :name, " +
			"postalcode = :postalcode, " +
			"address = :address, " +
			"map_lat = :map_lat, " +
			"map_lng = :map_lng, " +
			"map_location = :map_location, " +
			"space = :space, " +
			"display_size = :display_size, " +
			"max_storage_height = :max_storage_height, " +
			"max_storage_weight = :max_storage_weight, " +
			"min_rent_day = :min_rent_day, " +
			"currency = :currency, " +
			"unit = :unit, " +
			"palletnum = :palletnum, " +
			"has_rack_available = :has_rack_available, " +
			"total_rack_available = :total_rack_available, " +
			"rack_rent_price = :rack_rent_price, " +
			"has_stackable_available = :has_stackable_available, " +
			"total_stack_available = :total_stack_available, " +
			"stack_rent_price = :stack_rent_price, " +
			"additional_info = :additional_info, " +
			"country_id = :country_id, " +
			"country = :country_name, " +
			"state_id = :state_id, " +
			"state = :state_name, " +
			"city_id = :city_id, " +
			"city = :city_name, " +
			"district_id = :district_id, " +
			"district = :district_name, " +
			"occupancy = :occupancy, " +
			"status_available = :status_available, " +
			"unavailable_text = :unavailable_text, " +
			"updated_by = :updated_by, " +
			"updated_at = now() " +
			"	WHERE id = :id", nativeQuery = true)
	void updateWarehouse(@Param("user_id") int user_id,
						 @Param("name") String name,
						 @Param("postalcode") String postalcode,
						 @Param("address") String address,
						 @Param(("map_lat")) String map_lat,
						 @Param("map_lng") String map_lng,
						 @Param("map_location") String map_location,
						 @Param(("space")) Double space,
						 @Param("display_size") int display_size,
						 @Param("max_storage_height") double max_storage_height,
						 @Param("max_storage_weight") double max_storage_weight,
						 @Param("min_rent_day") int min_rent_day,
						 @Param("currency") String currency,
						 @Param("unit") int unit,
						 @Param("palletnum") double palletnum,
						 @Param("has_rack_available") int has_rack_available,
						 @Param("total_rack_available") int total_rack_available,
						 @Param("rack_rent_price") double rack_rent_price,
						 @Param("has_stackable_available") int has_stackable_available,
						 @Param("total_stack_available") int total_stack_available,
						 @Param("stack_rent_price") double stack_rent_price,
						 @Param("additional_info") String additional_info,
						 @Param("country_id") int country_id,
						 @Param("country_name") String country_name,
						 @Param("state_id") int state_id,
						 @Param("state_name") String state_name,
						 @Param("city_id") int city_id,
						 @Param("city_name") String city_name,
						 @Param("district_id") int district_id,
						 @Param("district_name") String district_name,
						 @Param("occupancy") Double occupancy,
						 @Param("status_available") int status_available,
						 @Param("unavailable_text") String unavailable_text,
						 @Param("id") int id,
						 @Param("updated_by") int updated_by);

	@Modifying
	@Query(value = "UPDATE warehouse w SET deleted_at = now(), name = CONCAT(name,'-',:id), slug = CONCAT(slug,'-',:id), deleted_by = :user_id WHERE id = :id", nativeQuery = true)
	void deleteById(@Param("id") int id, @Param("user_id") int user_id);

	@Modifying
	@Query(value = "UPDATE warehouse w SET status_verification = :status_verification, status = :display_status, approved_by = :admin_id, approved_at = now() WHERE id = :id", nativeQuery = true)
	void changeStatusVerificationActive(@Param("status_verification") int status_verification, @Param("display_status") int display_status, @Param("id") int id, @Param("admin_id") int admin_id);

	@Modifying
	@Query(value = "UPDATE warehouse w SET status_verification = :status_verification, status = :display_status, approved_by = :admin_id, approved_at = now(), is_first_class_warehouse = 0 WHERE id = :id", nativeQuery = true)
	void changeStatusVerificationInActive(@Param("status_verification") int status_verification, @Param("display_status") int display_status, @Param("id") int id, @Param("admin_id") int admin_id);

	@Query(value = "SELECT count(id) as jml FROM warehouse WHERE name = :warehouse_name", nativeQuery = true)
    int countByName(@Param("warehouse_name") String warehouse_name);

	@Query(value = "SELECT * FROM warehouse WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
	Optional<Warehouse> findById(@Param("id") int id);

	@Query(value = "SELECT count(id) FROM warehouse WHERE status_verification = 1 AND deleted_at IS NULL", nativeQuery = true)
    int getCountWarehousePendingVerification();

	@Modifying
	@Query(value = "UPDATE warehouse w SET status = :display_status WHERE id = :id", nativeQuery = true)
	void changeStatusActive(@Param("display_status") int display_status, @Param("id") int id);

	@Modifying
	@Query(value = "UPDATE warehouse w SET status = :display_status, is_first_class_warehouse = 0 WHERE id = :id", nativeQuery = true)
	void changeStatusInActive(@Param("display_status") int display_status, @Param("id") int id);

	@Query(value = "SELECT * FROM warehouse WHERE name = :name AND deleted_at IS NULL", nativeQuery = true)
	Optional<Warehouse> findByWarehouseName(@Param("name") String name);

	@Query(value = "SELECT * FROM warehouse WHERE is_first_class_warehouse = 1 AND deleted_at IS NULL AND status = 1 AND status_verification = 4", nativeQuery = true)
    List<Warehouse> findAllWarehouseFirstClass();
}
