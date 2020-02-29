package com.mospaze.werehouse.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mospaze.werehouse.entity.Media;
import com.mospaze.werehouse.entity.projector.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailWarehouse {
	private Object detail_warehouse;
	private List<WarehouseAutomation> warehouse_automation;
	private List<WarehouseCategory> warehouse_category;
	private List<WarehouseFeature> warehouse_feature;
	private List<WarehouseService> warehouse_service;
	private List<WarehouseSpace> warehouse_space;
	private List<WarehouseSpaceService> warehouse_space_service;
	private List<WarehouseType> warehouse_type;
	private List<WarehouseTemperature> warehouse_temperature;
	private List<WarehouseOperatingHour> warehouse_operation_hour;
	private List<Media> images;

	@Override
	public String toString() {
		return "DetailWarehouse{" +
				"detail_warehouse=" + detail_warehouse +
				", warehouse_automation=" + warehouse_automation +
				", warehouse_category=" + warehouse_category +
				", warehouse_feature=" + warehouse_feature +
				", warehouse_service=" + warehouse_service +
				", warehouse_space=" + warehouse_space +
				", warehouse_space_service=" + warehouse_space_service +
				", warehouse_type=" + warehouse_type +
				", warehouse_temperature=" + warehouse_temperature +
				", warehouse_operation_hour=" + warehouse_operation_hour +
				", images=" + images +
				'}';
	}
}
