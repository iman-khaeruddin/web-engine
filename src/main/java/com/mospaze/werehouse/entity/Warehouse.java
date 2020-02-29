package com.mospaze.werehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "warehouse")
@NamedNativeQueries({
		@NamedNativeQuery(
				name = "getAllWarehouseByFilter",
				query = "SELECT DISTINCT w.* FROM warehouse w",
				resultClass=Warehouse.class
		)
})
public class Warehouse implements Serializable{
	
	private static final long serialVersionUID = 7374051768244321850L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "user_id")
	private int user_id;
	
	@Column(name = "group_id")
	private int group_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "slug")
	private String slug;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "city")
	private String city;

	@Column(name = "district")
	private String district;
	
	@Column(name = "postalcode")
	private String postalcode;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "map_lat")
	private String map_lat;
	
	@Column(name = "map_lng")
	private String map_lng;
	
	@Column(name = "map_location")
	private String map_location;
	
	@Column(name = "space")
	private Double space;
	
	@Column(name = "display_size")
	private int display_size;
	
	@Column(name = "max_storage_height")
	private double max_storage_height;
	
	@Column(name = "max_storage_weight")
	private double max_storage_weight;
	
	@Column(name = "min_rent_day")
	private int min_rent_day;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "unit")
	private int unit;
	
	@Column(name = "palletnum")
	private Double palletnum;
	
	@Column(name = "has_rack_available")
	private int has_rack_available;
	
	@Column(name = "total_rack_available")
	private Integer total_rack_available;
	
	@Column(name = "rack_rent_price")
	private Double rack_rent_price;
	
	@Column(name = "has_stackable_available")
	private int has_stackable_available;
	
	@Column(name = "total_stack_available")
	private Integer total_stack_available;
	
	@Column(name = "stack_rent_price")
	private Double stack_rent_price;
	
	@Column(name = "additional_info")
	private String additional_info;
	
	@Column(name = "country_id")
	private Integer country_id;
	
	@Column(name = "state_id")
	private Integer state_id;
	
	@Column(name = "city_id")
	private Integer city_id;

	@Column(name = "district_id")
	private Integer district_id;
	
	@Column(name = "occupancy")
	private Double occupancy;
	
	@Column(name = "status_available")
	private int status_available;
	
	@Column(name = "unavailable_text")
	private String unavailable_text;
	
	@Column(name = "status_verification")
	private int status_verification;
	
	@Column(name = "status")
	private int status;
	
	@Column(name = "visit_count")
	private int visit_count;

	@Column(name = "is_partner")
	private int is_partner;

	@Column(name = "is_first_class_warehouse")
	private int is_first_class_warehouse;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "deleted_by")
	private Integer deleted_by;

	@Column(name = "approved_by")
	private Integer approved_by;

	@Column(name = "created_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created_at;

	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updated_at;

	@Column(name = "deleted_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date deleted_at;

	@Column(name = "approved_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approved_at;

}
