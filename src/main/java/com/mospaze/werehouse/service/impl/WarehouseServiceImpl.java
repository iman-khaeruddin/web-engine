package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.entity.projector.WarehouseByStatus;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.*;
import com.mospaze.werehouse.service.MsWarehouseSpaceService;
import com.mospaze.werehouse.service.WarehouseService;
import com.mospaze.werehouse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.WarehouseService
 */
@Service
public class WarehouseServiceImpl implements WarehouseService{

	@Autowired
	private WarehouseDao warehouseDao;

	@Autowired
	private CustomWarehouseDao customWarehouseDao;
	
	@Autowired
	private MsWarehouseAutomationService msWarehouseAutomationService;
	
	@Autowired
	private MsWarehouseCategoryService msWarehouseCategoryService;
	
	@Autowired
	private MsWarehouseFeatureService msWarehouseFeatureService;
	
	@Autowired
	private MsWarehouseServiceService msWarehouseServiceService;
	
	@Autowired
	private MsWarehouseSpaceService msWarehouseSpaceService;
	
	@Autowired
	private MsWarehouseSpaceServiceService msWarehouseSpaceServiceService;
	
	@Autowired
	private MsWarehouseTypeService msWarehouseTypeService;

	@Autowired
	private MsWarehouseTemperatureService msWarehouseTemperatureService;

	@Autowired
	private MsWarehouseOperatingHourService msWarehouseOperatingHourService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private UsersDao usersDao;

	@Autowired
	private MsCitiesDao msCitiesDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private WarehouseServiceDao warehouseServiceDao;

	@Autowired
	private WarehouseCategoryDao warehouseCategoryDao;

	@Autowired
	private WarehouseOperatingHourDao warehouseOperatingHourDao;

	@Autowired
	private Util util;

	private int COUNT;

	@Override
	@Deprecated
	public List<DetailWarehouse> GetAllDataWarehouse() {

		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		List<Warehouse> warehouses = warehouseDao.GetDataAllWarehouse();

		for(Warehouse warehouse : warehouses){
			DetailWarehouse detailWarehouse = new DetailWarehouse();

			detailWarehouse.setDetail_warehouse(warehouse);
			detailWarehouse.setWarehouse_automation(msWarehouseAutomationService.getMsWarehouseAutomationsById(warehouse.getId()));
			detailWarehouse.setWarehouse_category(msWarehouseCategoryService.getMsWarehouseCategoriesById(warehouse.getId()));
			detailWarehouse.setWarehouse_type(msWarehouseTypeService.getMsWarehouseTypesById(warehouse.getId()));
			detailWarehouse.setImages(mediaService.getMediaWarehouse(warehouse.getId()));

			detailWarehouses.add(detailWarehouse);
		}

		return detailWarehouses;
	}

	@Override
	public WarehousePageable getAllDataWarehousePageable(Integer page, Integer size) {

		WarehousePageable warehousePageable = new WarehousePageable();

		Pageable pageable = PageRequest.of(page, size);

		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		Page<Warehouse> warehouses = warehouseDao.GetDataAllWarehousePageable(pageable);

		if(warehouses.hasContent()){
            for(Warehouse warehouse : warehouses.getContent()){
                DetailWarehouse detailWarehouse = new DetailWarehouse();

                detailWarehouse.setDetail_warehouse(warehouse);
                detailWarehouse.setWarehouse_automation(msWarehouseAutomationService.getMsWarehouseAutomationsById(warehouse.getId()));
                detailWarehouse.setWarehouse_category(msWarehouseCategoryService.getMsWarehouseCategoriesById(warehouse.getId()));
                detailWarehouse.setWarehouse_type(msWarehouseTypeService.getMsWarehouseTypesById(warehouse.getId()));
                detailWarehouse.setImages(mediaService.getMediaWarehouse(warehouse.getId()));

                detailWarehouses.add(detailWarehouse);
            }
        }

		warehousePageable.setTotal_page(BigInteger.valueOf(warehouses.getTotalPages()));
		warehousePageable.setDetailWarehouses(detailWarehouses);
		warehousePageable.setTotal_element(BigInteger.valueOf(warehouses.getTotalElements()));

		return warehousePageable;
	}

	@Override
	public GeneralResponse getDetailWarehouse(int id) {
		Optional<Warehouse> warehouses = warehouseDao.findById(id);
		DetailWarehouse detailWarehouse = new DetailWarehouse();
		GeneralResponse generalResponse = new GeneralResponse();

		if(warehouses.isPresent()){
			detailWarehouse.setDetail_warehouse(warehouses.get());
			detailWarehouse.setWarehouse_automation(msWarehouseAutomationService.getMsWarehouseAutomationsById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_category(msWarehouseCategoryService.getMsWarehouseCategoriesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_feature(msWarehouseFeatureService.getMsWarehouseFeaturesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_service(msWarehouseServiceService.getMsWarehouseServicesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_space(msWarehouseSpaceService.getMsWarehouseSpacesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_space_service(msWarehouseSpaceServiceService.getMsWarehouseSpaceServicesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_type(msWarehouseTypeService.getMsWarehouseTypesById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_temperature(msWarehouseTemperatureService.getMsWarehouseTemperatureById(warehouses.get().getId()));
			detailWarehouse.setWarehouse_operation_hour(msWarehouseOperatingHourService.getMsWarehouseOperationHourById(warehouses.get().getId()));
			detailWarehouse.setImages(mediaService.getAllMediaWarehouse(warehouses.get().getId()));

			generalResponse.setData(detailWarehouse);
			generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
		} else {
			generalResponse.setCode(Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getEnMessage(generalResponse.getCode()));
		}

		return generalResponse;
	}

	@Override
	@Deprecated
	public List<DetailWarehouse> GetAllDataWarehouse(Integer cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking) {

		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		List<Warehouse> warehouses = customWarehouseDao.findByFilter(cities, size_min, size_max, feature, space_service, category, space, type, service, automation, unit, is_racking);

		for(Warehouse warehouse : warehouses){
			DetailWarehouse detailWarehouse = new DetailWarehouse();

			detailWarehouse.setDetail_warehouse(warehouse);
			detailWarehouse.setWarehouse_automation(msWarehouseAutomationService.getMsWarehouseAutomationsById(warehouse.getId()));
			detailWarehouse.setWarehouse_category(msWarehouseCategoryService.getMsWarehouseCategoriesById(warehouse.getId()));
			detailWarehouse.setWarehouse_type(msWarehouseTypeService.getMsWarehouseTypesById(warehouse.getId()));
			detailWarehouse.setImages(mediaService.getMediaWarehouse(warehouse.getId()));

			detailWarehouses.add(detailWarehouse);
		}
		return detailWarehouses;
	}

	@Override
	public WarehousePageable getAllDataByFilterWarehousePageable(String cities, Integer size_min, Integer size_max, String feature, String space_service, String category, String space, String type, String service, String automation, Integer unit, Integer is_racking, Integer page, Integer size, String temperature, String district, Integer is_first_class, Integer is_partner) {

		WarehousePageable warehousePageable = new WarehousePageable();

		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		List<Warehouse> warehouses = customWarehouseDao.findByFilter(cities, size_min, size_max, feature, space_service, category, space, type, service, automation, unit, is_racking, page, size, temperature, district, is_first_class, is_partner);

		for(Warehouse warehouse : warehouses){
			DetailWarehouse detailWarehouse = new DetailWarehouse();

			detailWarehouse.setDetail_warehouse(warehouse);
			detailWarehouse.setWarehouse_automation(msWarehouseAutomationService.getMsWarehouseAutomationsById(warehouse.getId()));
			detailWarehouse.setWarehouse_category(msWarehouseCategoryService.getMsWarehouseCategoriesById(warehouse.getId()));
			detailWarehouse.setWarehouse_type(msWarehouseTypeService.getMsWarehouseTypesById(warehouse.getId()));
			detailWarehouse.setImages(mediaService.getMediaWarehouse(warehouse.getId()));

			detailWarehouses.add(detailWarehouse);
		}

		warehousePageable.setDetailWarehouses(detailWarehouses);
		BigInteger total_element = customWarehouseDao.getTotalElementByFilter(cities, size_min, size_max, feature, space_service, category, space, type, service, automation, unit, is_racking, page, size, temperature, district, is_first_class, is_partner);
		double total_page =  total_element.doubleValue() / size;
		warehousePageable.setTotal_page(BigInteger.valueOf((int)Math.ceil(total_page)));
		warehousePageable.setTotal_element(total_element);

		if(warehousePageable.getDetailWarehouses().size() > 0){
			warehousePageable.setFrom((page * size) + 1);
			warehousePageable.setTo((page * warehousePageable.getDetailWarehouses().size()) + warehousePageable.getDetailWarehouses().size());

			if(warehousePageable.getDetailWarehouses().size() != size)
				warehousePageable.setTo(warehousePageable.getFrom() + warehousePageable.getDetailWarehouses().size() - 1);
		}

		return warehousePageable;
	}

	@Override
	public WarehousePageable getListWarehouseByStatus(Integer status, String name_city, ModelHasRole role_login_user, Integer page, Integer size) {
		WarehousePageable warehousePageable = new WarehousePageable();

		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		List<WarehouseByStatus> warehouseByStatuses = customWarehouseDao.findByStatus(status, name_city, role_login_user, page, size);

		for(WarehouseByStatus warehouseByStatus : warehouseByStatuses){
			DetailWarehouse detailWarehouse = new DetailWarehouse();

			detailWarehouse.setDetail_warehouse(warehouseByStatus);

			detailWarehouses.add(detailWarehouse);
		}

		warehousePageable.setDetailWarehouses(detailWarehouses);
		warehousePageable.setTotal_page(BigInteger.valueOf(customWarehouseDao.getTotalPageFindByStatus(status, name_city, role_login_user, page, size).intValue()));
		warehousePageable.setTotal_element(BigInteger.valueOf(customWarehouseDao.getTotalElementFindByStatus(status, name_city, role_login_user, page, size).intValue()));

		if(warehousePageable.getDetailWarehouses().size() > 0){
			warehousePageable.setFrom((page * size) + 1);
			warehousePageable.setTo((page * warehousePageable.getDetailWarehouses().size()) + warehousePageable.getDetailWarehouses().size());

			if(warehousePageable.getDetailWarehouses().size() != size)
				warehousePageable.setTo(warehousePageable.getFrom() + warehousePageable.getDetailWarehouses().size() - 1);
		}

		if(warehousePageable.getDetailWarehouses().size() > 0){
			warehousePageable.setFrom((page * size) + 1);
			warehousePageable.setTo((page * warehousePageable.getDetailWarehouses().size()) + warehousePageable.getDetailWarehouses().size());

			if(warehousePageable.getDetailWarehouses().size() != size)
				warehousePageable.setTo(warehousePageable.getFrom() + warehousePageable.getDetailWarehouses().size() - 1);
		}

		return warehousePageable;
	}

	@Override
	@Transactional
	public String updateWarehouse(WarehouseManagementRequest warehouseManagementRequest, String username) {
		Optional<Users> users = usersDao.getUserByPhone(username);
		Optional<ModelHasRole> userRole = modelHasRoleDao.findById(users.get().getId());
		Optional<Warehouse> warehouse = warehouseDao.findById(warehouseManagementRequest.getWarehouse_location_and_name().getId());

		if(warehouse.isPresent()) {
			if (userRole.get().getRole_id() == Constant.Role.BUYER.getRole() ||
					(userRole.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getSuper_user()) ||
					(userRole.get().getRole_id() == Constant.Role.SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getModel_id()))
				return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
		} else {
			return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
		}

		List<com.mospaze.werehouse.entity.projector.DetailCities> detailCities= msCitiesDao.getAllDistrictsEntitiesByIdDistrict(warehouseManagementRequest.getWarehouse_location_and_name().getDistrict_id());

		if(detailCities.size() == 0)
			return Constant.Code.ADDRESS_INVALID.getStatusCode();

		//update warehouse
		warehouseDao.updateWarehouse(warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_owner().getId(),
				warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_name(),
				warehouseManagementRequest.getWarehouse_location_and_name().getPostalcode(),
				warehouseManagementRequest.getWarehouse_location_and_name().getAddress(),
				warehouseManagementRequest.getWarehouse_location_and_name().getMap_lat(),
				warehouseManagementRequest.getWarehouse_location_and_name().getMap_lng(),
				warehouseManagementRequest.getWarehouse_location_and_name().getMap_location(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getSpace(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getDisplay_size(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getMax_storage_height(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getMax_storage_weight(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getMin_rent_day(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getCurrency(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getUnit().getId(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getPalletnum(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getHas_rack_available(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getTotal_rack_available(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getRack_rent_price(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getHas_stack_available(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getTotal_stack_available(),
				warehouseManagementRequest.getWarehouse_storage_and_pricing().getStack_rent_price(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getAdditional_information(),
				Integer.parseInt(detailCities.get(0).getId_countries()),
				detailCities.get(0).getCountries_name(),
				Integer.parseInt(detailCities.get(0).getId_state()),
				detailCities.get(0).getState_name(),
				Integer.parseInt(detailCities.get(0).getId_cities()),
				detailCities.get(0).getCities_name(),
				warehouseManagementRequest.getWarehouse_location_and_name().getDistrict_id(),
				detailCities.get(0).getDistrict_name(),
				warehouseManagementRequest.getWarehouse_size_and_facilities().getOccupancy_data(),
				warehouseManagementRequest.getStatus_available(),
				warehouseManagementRequest.getUnavailable_text(),
				warehouseManagementRequest.getWarehouse_location_and_name().getId(),
				userRole.get().getModel_id());

		//update warehouse category
		for(WarehouseCategoryModel msWarehouseCategory : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_category()){
			COUNT = msWarehouseCategoryService.countByCategoryId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
					msWarehouseCategory.getId());

			if(msWarehouseCategory.getStatus() == 1 && COUNT == 0){
				WarehouseCategory warehouseCategory = new WarehouseCategory();
				warehouseCategory.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseCategory.setCategory_id(msWarehouseCategory.getId());
				warehouseCategory.setInbound_price(msWarehouseCategory.getInbound_price());
				warehouseCategory.setOutbound_price(msWarehouseCategory.getOutbound_price());
				warehouseCategory.setInbound_uom(msWarehouseCategory.getInbound_uom());
				warehouseCategory.setOutbound_uom(msWarehouseCategory.getOutbound_uom());
				warehouseCategory.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseCategory.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseCategoryService.save(warehouseCategory);
			} else if(msWarehouseCategory.getStatus() == 0 && COUNT == 1){
				msWarehouseCategoryService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(), msWarehouseCategory.getId());
			} else if(msWarehouseCategory.getStatus() == 1 && COUNT == 1){
				WarehouseCategory warehouseCategory = warehouseCategoryDao.findByCategoryId(msWarehouseCategory.getId(),
						warehouseManagementRequest.getWarehouse_location_and_name().getId()).get();
				warehouseCategory.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseCategory.setCategory_id(msWarehouseCategory.getId());
				warehouseCategory.setInbound_price(msWarehouseCategory.getInbound_price());
				warehouseCategory.setOutbound_price(msWarehouseCategory.getOutbound_price());
				warehouseCategory.setInbound_uom(msWarehouseCategory.getInbound_uom());
				warehouseCategory.setOutbound_uom(msWarehouseCategory.getOutbound_uom());
				warehouseCategory.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseCategoryService.save(warehouseCategory);
			}
		}

		//update warehouse service
		for(WarehouseServiceModel msWarehouseService : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_service()){
			COUNT = msWarehouseServiceService.countByServiceId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
					msWarehouseService.getId());

			if(msWarehouseService.getStatus() == 1 && COUNT == 0){
				com.mospaze.werehouse.entity.WarehouseService warehouseService = new com.mospaze.werehouse.entity.WarehouseService();
				warehouseService.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseService.setServices_id(msWarehouseService.getId());
				warehouseService.setService_price(msWarehouseService.getService_price());
				warehouseService.setUom(msWarehouseService.getUom());
				warehouseService.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseService.setUpdated_at(new Timestamp(new Date().getTime()));

				msWarehouseServiceService.save(warehouseService);
			} else if(msWarehouseService.getStatus() == 0 && COUNT == 1){
				msWarehouseServiceService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(), msWarehouseService.getId());
			} else if(msWarehouseService.getStatus() == 1 && COUNT == 1){
				com.mospaze.werehouse.entity.WarehouseService warehouseService = warehouseServiceDao.findByServicesId(msWarehouseService.getId(),
						warehouseManagementRequest.getWarehouse_location_and_name().getId()).get();
				warehouseService.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseService.setServices_id(msWarehouseService.getId());
				warehouseService.setService_price(msWarehouseService.getService_price());
				warehouseService.setUom(msWarehouseService.getUom());
				warehouseService.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseService.setUpdated_at(new Timestamp(new Date().getTime()));

				msWarehouseServiceService.save(warehouseService);
			}
		}

		//update warehouse feature
		for(MsWarehouseFeature msWarehouseFeature : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_feature()){
			COUNT = msWarehouseFeatureService.countByFeatureId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
					msWarehouseFeature.getId());

			if(msWarehouseFeature.getStatus() == 1 && COUNT == 0){
				WarehouseFeatures warehouseFeatures = new WarehouseFeatures();
				warehouseFeatures.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseFeatures.setFeatures_id(msWarehouseFeature.getId());
				warehouseFeatures.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseFeatures.setUpdated_at(new Timestamp(new Date().getTime()));

				msWarehouseFeatureService.save(warehouseFeatures);
			} else if (msWarehouseFeature.getStatus() == 0 && COUNT == 1){
				msWarehouseFeatureService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(), msWarehouseFeature.getId());
			}
		}

		//update warehouse space
		for(MsWarehouseSpace msWarehouseSpace : warehouseManagementRequest.getWarehouse_storage_and_pricing().getWarehouse_space()){
			COUNT = msWarehouseSpaceService.countBySpaceId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
					msWarehouseSpace.getId());

			if(msWarehouseSpace.getStatus() == 1 && COUNT == 0){
				WarehouseSpace warehouseSpace = new WarehouseSpace();
				warehouseSpace.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseSpace.setSpace_id(msWarehouseSpace.getId());
				warehouseSpace.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseSpace.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseSpaceService.save(warehouseSpace);
			} else if(msWarehouseSpace.getStatus() == 0 && COUNT == 1){
				msWarehouseSpaceService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(), msWarehouseSpace.getId());
			}
		}

		//update warehouse space service
		for(com.mospaze.werehouse.entity.MsWarehouseSpaceService msWarehouseSpaceService : warehouseManagementRequest.getWarehouse_storage_and_pricing().getWarehouse_space_service()){
			COUNT = msWarehouseSpaceServiceService.countBySpaceServiceId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
					msWarehouseSpaceService.getId());
			if(msWarehouseSpaceService.getStatus() == 0 && COUNT == 1){
				msWarehouseSpaceServiceService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
						msWarehouseSpaceService.getId());
			} else if(msWarehouseSpaceService.getStatus() == 1 && COUNT == 0){
				WarehouseSpaceService warehouseSpaceService = new WarehouseSpaceService();
				warehouseSpaceService.setSpace_service_id(msWarehouseSpaceService.getId());
				warehouseSpaceService.setSpace_id(0);
				warehouseSpaceService.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
				warehouseSpaceService.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseSpaceService.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseSpaceServiceService.save(warehouseSpaceService);
			}
		}

		//update warehouse type
		msWarehouseTypeService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId());

		WarehouseType warehouseType = new WarehouseType();
		warehouseType.setTypes_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_type().getId());
		warehouseType.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
		warehouseType.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseType.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseTypeService.save(warehouseType);

		//update warehouse automation
		msWarehouseAutomationService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId());

		WarehouseAutomation warehouseAutomation = new WarehouseAutomation();
		warehouseAutomation.setAutomation_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_automation().getId());
		warehouseAutomation.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
		warehouseAutomation.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseAutomation.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseAutomationService.save(warehouseAutomation);

		//update warehouse temperature
		msWarehouseTemperatureService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId());

		WarehouseTemperature warehouseTemperature = new WarehouseTemperature();
		warehouseTemperature.setTemperature_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_temperature().getId());
		warehouseTemperature.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
		warehouseTemperature.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseTemperature.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseTemperatureService.save(warehouseTemperature);

		//update warehouse operation hour
		if(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_operating_hour() != null){
			for(WarehouseOperatingHourModel warehouseOperatingHourModel : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_operating_hour()){
				COUNT = msWarehouseOperatingHourService.countByOperationHourId(warehouseManagementRequest.getWarehouse_location_and_name().getId(),
						warehouseOperatingHourModel.getId());

				if(warehouseOperatingHourModel.getStatus() == 1 && COUNT == 0){
					WarehouseOperatingHour warehouseOperatingHour = new WarehouseOperatingHour();
					warehouseOperatingHour.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
					warehouseOperatingHour.setOperating_hour_id(warehouseOperatingHourModel.getId());
					warehouseOperatingHour.setClose(warehouseOperatingHourModel.getClose());
					warehouseOperatingHour.setOpen(warehouseOperatingHourModel.getOpen());
					warehouseOperatingHour.setNumber_of_shift(warehouseOperatingHourModel.getNumber_of_shift());
					warehouseOperatingHour.setRegular_overtime(warehouseOperatingHourModel.getRegular_overtime());
					warehouseOperatingHour.setHoliday_overtime(warehouseOperatingHourModel.getHoliday_overtime());
					warehouseOperatingHour.setCreated_at(new Timestamp(new Date().getTime()));
					warehouseOperatingHour.setUpdated_at(new Timestamp(new Date().getTime()));
					msWarehouseOperatingHourService.save(warehouseOperatingHour);
				} else if(warehouseOperatingHourModel.getStatus() == 0 && COUNT == 1){
					msWarehouseOperatingHourService.delete(warehouseManagementRequest.getWarehouse_location_and_name().getId(), warehouseOperatingHourModel.getId());
				} else if(warehouseOperatingHourModel.getStatus() == 1 && COUNT == 1){
					WarehouseOperatingHour warehouseOperatingHour = warehouseOperatingHourDao.findByOperatingHourId(warehouseOperatingHourModel.getId(),
							warehouseManagementRequest.getWarehouse_location_and_name().getId()).get();
					warehouseOperatingHour.setWarehouse_id(warehouseManagementRequest.getWarehouse_location_and_name().getId());
					warehouseOperatingHour.setOperating_hour_id(warehouseOperatingHourModel.getId());
					warehouseOperatingHour.setClose(warehouseOperatingHourModel.getClose());
					warehouseOperatingHour.setOpen(warehouseOperatingHourModel.getOpen());
					warehouseOperatingHour.setNumber_of_shift(warehouseOperatingHourModel.getNumber_of_shift());
					warehouseOperatingHour.setRegular_overtime(warehouseOperatingHourModel.getRegular_overtime());
					warehouseOperatingHour.setHoliday_overtime(warehouseOperatingHourModel.getHoliday_overtime());
					warehouseOperatingHour.setCreated_at(new Timestamp(new Date().getTime()));
					warehouseOperatingHour.setUpdated_at(new Timestamp(new Date().getTime()));
					msWarehouseOperatingHourService.save(warehouseOperatingHour);
				}
			}
		}

		//set main image
		mediaService.removeMainImage(warehouseManagementRequest.getWarehouse_location_and_name().getId());
		mediaService.setMainImage(warehouseManagementRequest.getMain_image());

		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteWarehouse(int id, String username) {
		Optional<ModelHasRole> userRole = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());
		Optional<Warehouse> warehouse = warehouseDao.findById(id);

		if(warehouse.isPresent()) {
			if (userRole.get().getRole_id() == Constant.Role.BUYER.getRole() ||
					(userRole.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getSuper_user()) ||
					(userRole.get().getRole_id() == Constant.Role.SELLER.getRole() && warehouse.get().getUser_id() != userRole.get().getModel_id()))
				return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
		} else {
			return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
		}

		warehouseDao.deleteById(id, userRole.get().getModel_id());
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String changeStatusVerificationWarehouse(Integer status_verification, int display_status, int id, String username, int is_send_notification, String notes) {

		if(!Constant.getStatusVerification().contains(status_verification))
			return Constant.Code.ACTION_INVALID.getStatusCode();

		if(display_status != 1 && display_status != 0)
			return Constant.Code.ACTION_INVALID.getStatusCode();

		Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());
		Optional<Warehouse> warehouse = warehouseDao.findById(id);

		Notification notification = new Notification();

		if(warehouse.isPresent()) {
			if (role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() ||
					role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole()){

				if(display_status == 1)
					warehouseDao.changeStatusVerificationActive(status_verification, display_status, id, role_login_user.get().getModel_id());
				else
					warehouseDao.changeStatusVerificationInActive(status_verification, display_status, id, role_login_user.get().getModel_id());

				//insert notification
				if(warehouse.get().getStatus_verification() != Constant.StatusVerification.APPROVED.getStatus() &&
						status_verification == Constant.StatusVerification.APPROVED.getStatus()){
					notification.setId_message("Verifikasi berhasil untuk warehouse " + warehouse.get().getName());
					notification.setEn_message("Warehouse " + warehouse.get().getName() + " has been approved");
					notification.setUser_id(warehouse.get().getUser_id());
					notification.setType(Constant.NotificationType.WAREHOUSE.getType());
					notification.setModel_id(warehouse.get().getId());
					notification.setCreated_at(new Date());
					notificationDao.save(notification);

					/*if(is_send_notification == 1)
						new Thread(new Runnable() {
							@Override
							public void run() {
								Users users = usersDao.findById(warehouse.get().getUser_id()).get();

								Map<String, String> email = new HashMap<>();

								String URL = "";

								email.put("to", users.getEmail());
								email.put("subject", "Verification Warehouse Status");
								email.put("content", Constant.configProperties.get(Constant.EMAIL_WORDING_APPROVED_WAREHOUSE)
										.replace("[name]", users.getFirst_name().toUpperCase() + " " + users.getLast_name().toUpperCase())
										.replace("[url]", URL)
										.replace("[url_contact_us]", Constant.configProperties.get(Constant.URL_CONTACT_US)));

								util.sendEmail(email);
							}
						}).start();*/

				} else if(warehouse.get().getStatus_verification() != Constant.StatusVerification.REJECTED.getStatus() &&
						status_verification == Constant.StatusVerification.REJECTED.getStatus()){
					notification.setId_message("Verifikasi ditolak untuk warehouse " + warehouse.get().getName());
					notification.setEn_message("Warehouse " + warehouse.get().getName() + " has been rejected");
					notification.setUser_id(warehouse.get().getUser_id());
					notification.setType(Constant.NotificationType.WAREHOUSE.getType());
					notification.setModel_id(warehouse.get().getId());
					notification.setCreated_at(new Date());
					notificationDao.save(notification);

					/*if(is_send_notification == 1)
						new Thread(new Runnable() {
							@Override
							public void run() {
								Map<String, String> email = new HashMap<>();
								email.put("to", usersDao.findById(warehouse.get().getUser_id()).get().getEmail());
								email.put("subject", "Verification Warehouse Status");
								email.put("content", Constant.configProperties.get(Constant.EMAIL_WORDING_REJECTED_WAREHOUSE));

								util.sendEmail(email);
							}
						}).start();*/
				}
			} else {
				return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
			}

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
		}

	}

	@Override
	public GeneralResponse getDetailWarehouseById(int id, String username) {
		GeneralResponse generalResponse = new GeneralResponse();
		WarehouseManagementRequest detailWarehouseResponse = new WarehouseManagementRequest();

		Optional<ModelHasRole> userRole = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

		if(userRole.get().getRole_id() == Constant.Role.BUYER.getRole()){
			generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

			return generalResponse;
		}

		generalResponse = getDetailWarehouse(id);

		DetailWarehouse detailWarehouse = (DetailWarehouse) generalResponse.getData();

		if(generalResponse.getData() != null){

			Warehouse warehouse = (Warehouse) detailWarehouse.getDetail_warehouse();

			if((userRole.get().getRole_id() == Constant.Role.SUB_SELLER.getRole() && warehouse.getUser_id() != userRole.get().getSuper_user()) ||
					(userRole.get().getRole_id() == Constant.Role.SELLER.getRole() && warehouse.getUser_id() != userRole.get().getModel_id())){

				generalResponse = new GeneralResponse();
				generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
				generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
				generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

				return generalResponse;
			}

			//warehouse location and name
			WarehouseLocationAndName warehouseLocationAndName = new WarehouseLocationAndName();
			warehouseLocationAndName.setId(warehouse.getId());
			warehouseLocationAndName.setAddress(warehouse.getAddress());

			if(warehouse.getDistrict_id() != null){
				List<com.mospaze.werehouse.entity.projector.DetailCities> detailCities = msCitiesDao.getAllDistrictsEntitiesByIdDistrict(warehouse.getDistrict_id());
				for(com.mospaze.werehouse.entity.projector.DetailCities detailCities1 : detailCities){
					warehouseLocationAndName.setCity_id(Integer.valueOf(detailCities1.getId_cities()));
					warehouseLocationAndName.setCity_name(detailCities1.getCities_name());
					warehouseLocationAndName.setState_id(Integer.valueOf(detailCities1.getId_state()));
					warehouseLocationAndName.setState_name(detailCities1.getState_name());
					warehouseLocationAndName.setCountry_id(Integer.valueOf(detailCities1.getId_countries()));
					warehouseLocationAndName.setCountry_name(detailCities1.getCountries_name());
					warehouseLocationAndName.setDistrict_id(Integer.valueOf(detailCities1.getId_district()));
					warehouseLocationAndName.setDistrict_name(detailCities1.getDistrict_name());
				}
			}

			warehouseLocationAndName.setMap_lat(warehouse.getMap_lat());
			warehouseLocationAndName.setMap_lng(warehouse.getMap_lng());
			warehouseLocationAndName.setMap_location(warehouse.getMap_location());
			warehouseLocationAndName.setPostalcode(warehouse.getPostalcode());
			warehouseLocationAndName.setWarehouse_name(warehouse.getName());
			Optional<Users> usr = usersDao.findById(warehouse.getUser_id());

			if(usr.isPresent()){
				Users users = usr.get();
				warehouseLocationAndName.setWarehouse_owner(new WarehouseOwner(users.getId(), users.getFirst_name().concat(" " + users.getLast_name())));
			}

			//warehouse size and facilities
			WarehouseSizeAndFacilities warehouseSizeAndFacilities = new WarehouseSizeAndFacilities();
			warehouseSizeAndFacilities.setAdditional_information(warehouse.getAdditional_info());
			warehouseSizeAndFacilities.setDisplay_size(warehouse.getDisplay_size());
			warehouseSizeAndFacilities.setMax_storage_height(warehouse.getMax_storage_height());
			warehouseSizeAndFacilities.setMax_storage_weight(warehouse.getMax_storage_weight());
			warehouseSizeAndFacilities.setMin_rent_day(warehouse.getMin_rent_day());
			warehouseSizeAndFacilities.setOccupancy_data(warehouse.getOccupancy());
			warehouseSizeAndFacilities.setPalletnum(warehouse.getPalletnum());
			warehouseSizeAndFacilities.setSpace(warehouse.getSpace());

			for(com.mospaze.werehouse.entity.projector.WarehouseAutomation warehouseAutomation : detailWarehouse.getWarehouse_automation()){
				MsWarehouseAutomation msWarehouseAutomation = new MsWarehouseAutomation();
				msWarehouseAutomation.setStatus(1);
				msWarehouseAutomation.setId(warehouseAutomation.getId());
				msWarehouseAutomation.setAutomation_name(warehouseAutomation.getAutomation_name());
				warehouseSizeAndFacilities.setWarehouse_automation(msWarehouseAutomation);
			}

			for(com.mospaze.werehouse.entity.projector.WarehouseType warehouseType : detailWarehouse.getWarehouse_type()){
				MsWarehouseType msWarehouseType = new MsWarehouseType();
				msWarehouseType.setStatus(1);
				msWarehouseType.setId(warehouseType.getId());
				msWarehouseType.setType_name(warehouseType.getType_name());
				warehouseSizeAndFacilities.setWarehouse_type(msWarehouseType);
			}

			for(com.mospaze.werehouse.entity.projector.WarehouseTemperature warehouseTemperature : detailWarehouse.getWarehouse_temperature()){
				MsWarehouseTemperature msWarehouseTemperature = new MsWarehouseTemperature();
				msWarehouseTemperature.setStatus(1);
				msWarehouseTemperature.setId(warehouseTemperature.getId());
				msWarehouseTemperature.setTemperature_name(warehouseTemperature.getTemperature_name());
				warehouseSizeAndFacilities.setWarehouse_temperature(msWarehouseTemperature);
			}

			List<WarehouseCategoryModel> msWarehouseCategories = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseCategory warehouseCategory : detailWarehouse.getWarehouse_category()){
				WarehouseCategoryModel msWarehouseCategory = new WarehouseCategoryModel();
				msWarehouseCategory.setStatus(1);
				msWarehouseCategory.setId(warehouseCategory.getId());
				msWarehouseCategory.setCategory_name(warehouseCategory.getCategory_name());
				msWarehouseCategory.setInbound_price(warehouseCategory.getInbound_price());
				msWarehouseCategory.setOutbound_price(warehouseCategory.getOutbound_price());
				msWarehouseCategory.setInbound_uom(warehouseCategory.getInbound_uom());
				msWarehouseCategory.setOutbound_uom(warehouseCategory.getOutbound_uom());
				msWarehouseCategories.add(msWarehouseCategory);
			}
			warehouseSizeAndFacilities.setWarehouse_category(msWarehouseCategories);

			List<WarehouseServiceModel> msWarehouseServices = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseService warehouseService : detailWarehouse.getWarehouse_service()){
				WarehouseServiceModel msWarehouseService = new WarehouseServiceModel();
				msWarehouseService.setStatus(1);
				msWarehouseService.setId(warehouseService.getId());
				msWarehouseService.setServices_name(warehouseService.getServices_name());
				msWarehouseService.setService_price(warehouseService.getService_price());
				msWarehouseService.setUom(warehouseService.getUom());
				msWarehouseServices.add(msWarehouseService);
			}
			warehouseSizeAndFacilities.setWarehouse_service(msWarehouseServices);

			List<MsWarehouseFeature> msWarehouseFeatures = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseFeature warehouseFeature : detailWarehouse.getWarehouse_feature()){
				MsWarehouseFeature msWarehouseFeature = new MsWarehouseFeature();
				msWarehouseFeature.setStatus(1);
				msWarehouseFeature.setId(warehouseFeature.getId());
				msWarehouseFeature.setFeatures_name(warehouseFeature.getFeatures_name());
				msWarehouseFeatures.add(msWarehouseFeature);
			}
			warehouseSizeAndFacilities.setWarehouse_feature(msWarehouseFeatures);

			List<WarehouseOperatingHourModel> warehouseOperatingHourModels = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseOperatingHour warehouseOperatingHour : detailWarehouse.getWarehouse_operation_hour()){
				WarehouseOperatingHourModel msWarehouseService = new WarehouseOperatingHourModel();
				msWarehouseService.setStatus(1);
				msWarehouseService.setId(warehouseOperatingHour.getId());
				msWarehouseService.setOperation_hour_name(warehouseOperatingHour.getOperating_hour_name());
				warehouseOperatingHourModels.add(msWarehouseService);
			}
			warehouseSizeAndFacilities.setWarehouse_operating_hour(warehouseOperatingHourModels);

			//warehouse storage method and pricing
			WarehouseStorageMethodAndPricing warehouseStorageMethodAndPricing = new WarehouseStorageMethodAndPricing();
			warehouseStorageMethodAndPricing.setCurrency(warehouse.getCurrency());
			warehouseStorageMethodAndPricing.setHas_rack_available(warehouse.getHas_rack_available());
			warehouseStorageMethodAndPricing.setTotal_rack_available(warehouse.getTotal_rack_available());
			warehouseStorageMethodAndPricing.setRack_rent_price(warehouse.getRack_rent_price());
			warehouseStorageMethodAndPricing.setHas_stack_available(warehouse.getHas_stackable_available());
			warehouseStorageMethodAndPricing.setTotal_stack_available(warehouse.getTotal_stack_available());
			warehouseStorageMethodAndPricing.setStack_rent_price(warehouse.getStack_rent_price());

			MsUnit msUnit = new MsUnit();
			msUnit.setId(warehouse.getUnit());
			warehouseStorageMethodAndPricing.setUnit(msUnit);

			List<com.mospaze.werehouse.entity.MsWarehouseSpaceService> msWarehouseSpaceServices = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseSpaceService warehouseSpaceService : detailWarehouse.getWarehouse_space_service()){
				com.mospaze.werehouse.entity.MsWarehouseSpaceService msWarehouseSpaceService = new com.mospaze.werehouse.entity.MsWarehouseSpaceService();
				msWarehouseSpaceService.setStatus(1);
				msWarehouseSpaceService.setId(warehouseSpaceService.getId());
				msWarehouseSpaceService.setSpace_service_name(warehouseSpaceService.getSpace_service_name());
				msWarehouseSpaceServices.add(msWarehouseSpaceService);
			}
			warehouseStorageMethodAndPricing.setWarehouse_space_service(msWarehouseSpaceServices);

			List<MsWarehouseSpace> msWarehouseSpaces = new ArrayList<>();
			for(com.mospaze.werehouse.entity.projector.WarehouseSpace warehouseService : detailWarehouse.getWarehouse_space()){
				MsWarehouseSpace msWarehouseSpace = new MsWarehouseSpace();
				msWarehouseSpace.setStatus(1);
				msWarehouseSpace.setId(warehouseService.getId());
				msWarehouseSpace.setSpace_name(warehouseService.getSpace_name());
				msWarehouseSpaces.add(msWarehouseSpace);
			}
			warehouseStorageMethodAndPricing.setWarehouse_space(msWarehouseSpaces);

			detailWarehouseResponse.setIs_partner(warehouse.getIs_partner());
			detailWarehouseResponse.setIs_first_class_warehouse(warehouse.getIs_first_class_warehouse());
			detailWarehouseResponse.setWarehouse_location_and_name(warehouseLocationAndName);
			detailWarehouseResponse.setWarehouse_size_and_facilities(warehouseSizeAndFacilities);
			detailWarehouseResponse.setWarehouse_storage_and_pricing(warehouseStorageMethodAndPricing);
			detailWarehouseResponse.setMedia(detailWarehouse.getImages());
			detailWarehouseResponse.setStatus_available(warehouse.getStatus_available());
			detailWarehouseResponse.setUnavailable_text(warehouse.getUnavailable_text());
			detailWarehouseResponse.setStatus(warehouse.getStatus());
			detailWarehouseResponse.setStatus_verification(warehouse.getStatus_verification());

			generalResponse.setData(detailWarehouseResponse);
			generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
		} else {
			generalResponse.setCode(Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
		}

		return generalResponse;
	}

	@Override
	@Transactional
	public Warehouse insertWarehouse(WarehouseManagementRequest warehouseManagementRequest, Optional<ModelHasRole> role_login_user) {
		//insert warehouse
		Warehouse warehouse = new Warehouse();

		if(role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() ||
				role_login_user.get().getRole_id() == Constant.Role.ADMIN.getRole() ||
				role_login_user.get().getRole_id() == Constant.Role.SUPER_ADMIN.getRole())
			warehouse.setUser_id(warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_owner().getId());
		else
			warehouse.setUser_id(role_login_user.get().getSuper_user());

		warehouse.setName(warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_name());
		warehouse.setAddress(warehouseManagementRequest.getWarehouse_location_and_name().getAddress());
		warehouse.setCurrency(warehouseManagementRequest.getWarehouse_storage_and_pricing().getCurrency());
		warehouse.setDisplay_size(warehouseManagementRequest.getWarehouse_size_and_facilities().getDisplay_size());
		warehouse.setHas_rack_available(warehouseManagementRequest.getWarehouse_storage_and_pricing().getHas_rack_available());
		warehouse.setTotal_rack_available(warehouseManagementRequest.getWarehouse_storage_and_pricing().getTotal_rack_available());
		warehouse.setRack_rent_price(warehouseManagementRequest.getWarehouse_storage_and_pricing().getRack_rent_price());
		warehouse.setHas_stackable_available(warehouseManagementRequest.getWarehouse_storage_and_pricing().getHas_stack_available());
		warehouse.setTotal_stack_available(warehouseManagementRequest.getWarehouse_storage_and_pricing().getTotal_stack_available());
		warehouse.setStack_rent_price(warehouseManagementRequest.getWarehouse_storage_and_pricing().getStack_rent_price());
		warehouse.setMap_lat(warehouseManagementRequest.getWarehouse_location_and_name().getMap_lat());
		warehouse.setMap_lng(warehouseManagementRequest.getWarehouse_location_and_name().getMap_lng());
		warehouse.setMap_location(warehouseManagementRequest.getWarehouse_location_and_name().getMap_location());
		warehouse.setMax_storage_height(warehouseManagementRequest.getWarehouse_size_and_facilities().getMax_storage_height());
		warehouse.setMax_storage_weight(warehouseManagementRequest.getWarehouse_size_and_facilities().getMax_storage_weight());
		warehouse.setMin_rent_day(warehouseManagementRequest.getWarehouse_size_and_facilities().getMin_rent_day());
		warehouse.setPostalcode(warehouseManagementRequest.getWarehouse_location_and_name().getPostalcode());
		warehouse.setSlug(warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_name().toLowerCase().replace(" ", "-"));
		warehouse.setStatus(warehouseManagementRequest.getStatus());
		warehouse.setStatus_verification(Constant.StatusVerification.PENDING_VERIFICATION.getStatus());
		warehouse.setStatus_available(warehouseManagementRequest.getStatus_available());
		warehouse.setUnit(warehouseManagementRequest.getWarehouse_storage_and_pricing().getUnit().getId());
		warehouse.setVisit_count(0);
		warehouse.setAdditional_info(warehouseManagementRequest.getWarehouse_size_and_facilities().getAdditional_information());

		List<com.mospaze.werehouse.entity.projector.DetailCities> detailCities= msCitiesDao.getAllDistrictsEntitiesByIdDistrict(warehouseManagementRequest.getWarehouse_location_and_name().getDistrict_id());
		for(com.mospaze.werehouse.entity.projector.DetailCities detailCities1 : detailCities){
			warehouse.setCity(detailCities1.getCities_name());
			warehouse.setState(detailCities1.getState_name());
			warehouse.setCountry(detailCities1.getCountries_name());
			warehouse.setDistrict(detailCities1.getDistrict_name());

			warehouse.setDistrict_id(warehouseManagementRequest.getWarehouse_location_and_name().getDistrict_id());
			warehouse.setCity_id(Integer.valueOf(detailCities1.getId_cities()));
			warehouse.setState_id(Integer.valueOf(detailCities1.getId_state()));
			warehouse.setCountry_id(Integer.valueOf(detailCities1.getId_countries()));
		}

		warehouse.setOccupancy(warehouseManagementRequest.getWarehouse_size_and_facilities().getOccupancy_data());
		warehouse.setPalletnum(warehouseManagementRequest.getWarehouse_size_and_facilities().getPalletnum());
		warehouse.setSpace(warehouseManagementRequest.getWarehouse_size_and_facilities().getSpace());
		warehouse.setUnavailable_text(warehouseManagementRequest.getUnavailable_text());
		warehouse.setCreated_at(new Date());
		warehouse.setUpdated_at(new Date());
		warehouse.setCreated_by(role_login_user.get().getModel_id());
		warehouse = warehouseDao.save(warehouse);

		//insert warehouse category
		for (WarehouseCategoryModel msWarehouseCategory : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_category()){
			if(msWarehouseCategory.getStatus() == 1){
				WarehouseCategory warehouseCategory = new WarehouseCategory();
				warehouseCategory.setWarehouse_id(warehouse.getId());
				warehouseCategory.setCategory_id(msWarehouseCategory.getId());
				warehouseCategory.setInbound_price(msWarehouseCategory.getInbound_price());
				warehouseCategory.setOutbound_price(msWarehouseCategory.getOutbound_price());
				warehouseCategory.setInbound_uom(msWarehouseCategory.getInbound_uom());
				warehouseCategory.setOutbound_uom(msWarehouseCategory.getOutbound_uom());
				warehouseCategory.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseCategory.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseCategoryService.save(warehouseCategory);
			}
		}

		//insert warehouse service
		for(WarehouseServiceModel msWarehouseService : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_service()){
			if(msWarehouseService.getStatus() == 1){
				com.mospaze.werehouse.entity.WarehouseService warehouseService = new com.mospaze.werehouse.entity.WarehouseService();
				warehouseService.setWarehouse_id(warehouse.getId());
				warehouseService.setServices_id(msWarehouseService.getId());
				warehouseService.setService_price(msWarehouseService.getService_price());
				warehouseService.setUom(msWarehouseService.getUom());
				warehouseService.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseService.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseServiceService.save(warehouseService);
			}
		}

		//insert warehouse features
		for(MsWarehouseFeature msWarehouseFeature : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_feature()){
			if(msWarehouseFeature.getStatus() == 1){
				WarehouseFeatures warehouseFeatures = new WarehouseFeatures();
				warehouseFeatures.setWarehouse_id(warehouse.getId());
				warehouseFeatures.setFeatures_id(msWarehouseFeature.getId());
				warehouseFeatures.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseFeatures.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseFeatureService.save(warehouseFeatures);
			}
		}

		//insert warehouse type
		WarehouseType warehouseType = new WarehouseType();
		warehouseType.setWarehouse_id(warehouse.getId());
		warehouseType.setTypes_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_type().getId());
		warehouseType.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseType.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseTypeService.save(warehouseType);

		//insert warehouse automation
		WarehouseAutomation warehouseAutomation = new WarehouseAutomation();
		warehouseAutomation.setWarehouse_id(warehouse.getId());
		warehouseAutomation.setAutomation_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_automation().getId());
		warehouseAutomation.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseAutomation.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseAutomationService.save(warehouseAutomation);

		//insert warehouse space
		for(MsWarehouseSpace msWarehouseSpace : warehouseManagementRequest.getWarehouse_storage_and_pricing().getWarehouse_space()){
			if(msWarehouseSpace.getStatus() == 1){
				WarehouseSpace warehouseSpace = new WarehouseSpace();
				warehouseSpace.setWarehouse_id(warehouse.getId());
				warehouseSpace.setSpace_id(msWarehouseSpace.getId());
				warehouseSpace.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseSpace.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseSpaceService.save(warehouseSpace);
			}
		}

		//insert warehouse space service
		for(com.mospaze.werehouse.entity.MsWarehouseSpaceService msWarehouseSpaceService : warehouseManagementRequest.getWarehouse_storage_and_pricing().getWarehouse_space_service()){
			if(msWarehouseSpaceService.getStatus() == 1){
				WarehouseSpaceService warehouseSpaceService = new WarehouseSpaceService();
				warehouseSpaceService.setWarehouse_id(warehouse.getId());
				warehouseSpaceService.setSpace_service_id(msWarehouseSpaceService.getId());
				warehouseSpaceService.setCreated_at(new Timestamp(new Date().getTime()));
				warehouseSpaceService.setUpdated_at(new Timestamp(new Date().getTime()));
				msWarehouseSpaceServiceService.save(warehouseSpaceService);
			}
		}

		//insert warehouse temperature
		WarehouseTemperature warehouseTemperature = new WarehouseTemperature();
		warehouseTemperature.setWarehouse_id(warehouse.getId());
		warehouseTemperature.setTemperature_id(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_temperature().getId());
		warehouseTemperature.setCreated_at(new Timestamp(new Date().getTime()));
		warehouseTemperature.setUpdated_at(new Timestamp(new Date().getTime()));
		msWarehouseTemperatureService.save(warehouseTemperature);

		//insert warehouse operating hour
		if(warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_operating_hour() != null){
			for(WarehouseOperatingHourModel warehouseOperatingHourModel : warehouseManagementRequest.getWarehouse_size_and_facilities().getWarehouse_operating_hour()){
				if(warehouseOperatingHourModel.getStatus() == 1){
					WarehouseOperatingHour warehouseOperatingHour = new WarehouseOperatingHour();
					warehouseOperatingHour.setWarehouse_id(warehouse.getId());
					warehouseOperatingHour.setOperating_hour_id(warehouseOperatingHourModel.getId());
					warehouseOperatingHour.setOpen(warehouseOperatingHourModel.getOpen());
					warehouseOperatingHour.setClose(warehouseOperatingHourModel.getClose());
					warehouseOperatingHour.setNumber_of_shift(warehouseOperatingHourModel.getNumber_of_shift());
					warehouseOperatingHour.setRegular_overtime(warehouseOperatingHourModel.getRegular_overtime());
					warehouseOperatingHour.setHoliday_overtime(warehouseOperatingHourModel.getHoliday_overtime());
					warehouseOperatingHour.setCreated_at(new Timestamp(new Date().getTime()));
					warehouseOperatingHour.setUpdated_at(new Timestamp(new Date().getTime()));
					msWarehouseOperatingHourService.save(warehouseOperatingHour);
				}
			}
		}

		//insert notification
		Notification notification = new Notification();
		notification.setUser_id(0);
		notification.setType(Constant.NotificationType.WAREHOUSE.getType());
		notification.setModel_id(warehouse.getId());
		notification.setId_message("Warehouse " + warehouse.getName() + " menunggu verifikasi");
		notification.setEn_message("Warehouse " + warehouse.getName() + " waiting verification");
		notification.setCreated_at(new Date());
		notificationDao.save(notification);

		return warehouse;
	}

	@Override
	@Deprecated
	public NotificationResponse getNotificationWarehouse(ModelHasRole role_login_user) {
		NotificationResponse notificationResponse = new NotificationResponse();

		if(role_login_user.getRole_id() == Constant.Role.ADMIN.getRole() ||
				role_login_user.getRole_id() == Constant.Role.SUPER_ADMIN.getRole()){
//			notificationResponse.setNotifications(notificationDao.getNotification(0, Constant.NotificationType.WAREHOUSE.getType()));
			notificationResponse.setTotal_notification(warehouseDao.getCountWarehousePendingVerification());
		}

		/*if(role_login_user.getRole_id() == Constant.Role.SELLER.getRole()){
			notificationResponse.setNotifications(notificationDao.getNotification(role_login_user.getRole_id(), Constant.NotificationType.WAREHOUSE.getType()));
			notificationResponse.setTotal_notification(notificationResponse.getNotifications().size());
		}

		if(role_login_user.getRole_id() == Constant.Role.SUB_SELLER.getRole()){
			notificationResponse.setNotifications(notificationDao.getNotification(role_login_user.getSuper_user(), Constant.NotificationType.WAREHOUSE.getType()));
			notificationResponse.setTotal_notification(notificationResponse.getNotifications().size());
		}*/

		return notificationResponse;
	}

	@Override
	@Transactional
	public String changeWarehousePartner(int status, int id, JWT jwt) {
		if (jwt.getRole_id() == Constant.Role.ADMIN.getRole() ||
				jwt.getRole_id() == Constant.Role.SUPER_ADMIN.getRole()){

			Optional<Warehouse> warehouse = warehouseDao.findById(id);

			if(warehouse.isPresent()){
				Warehouse warehouse1 = warehouse.get();

				if(warehouse1.getStatus_verification() != Constant.StatusVerification.APPROVED.getStatus() ||
					warehouse1.getStatus() == 0)
					return Constant.Code.WAREHOUSE_NOT_ACTIVE.getStatusCode();

				warehouse1.setIs_partner(status);
				warehouse1.setUpdated_at(new Date());
				warehouse1.setUpdated_by(jwt.getUser_id());

				warehouseDao.save(warehouse1);

				return Constant.Code.SUCCESS.getStatusCode();
			} else {
				return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
			}
		} else {
			return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
		}
	}

	@Override
	public GeneralResponse getAllWarehouseFirstClass() {
		GeneralResponse generalResponse = new GeneralResponse();
		List<DetailWarehouse> detailWarehouses = new ArrayList<>();

		List<Warehouse> warehouses = warehouseDao.findAllWarehouseFirstClass();

		for(Warehouse warehouse : warehouses){
			DetailWarehouse detailWarehouse = new DetailWarehouse();

			detailWarehouse.setDetail_warehouse(warehouse);
			detailWarehouse.setImages(mediaService.getMediaWarehouse(warehouse.getId()));

			detailWarehouses.add(detailWarehouse);
		}

		if (detailWarehouses.size() == 0) {
			generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
		} else {
			generalResponse.setData(detailWarehouses);
			generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
		}

		return generalResponse;
	}

	@Override
	public String changeWarehouseFirstClass(int status, int id, JWT jwt) {
		if (jwt.getRole_id() == Constant.Role.ADMIN.getRole() ||
				jwt.getRole_id() == Constant.Role.SUPER_ADMIN.getRole()){

			Optional<Warehouse> warehouse = warehouseDao.findById(id);

			if(warehouse.isPresent()){
				Warehouse warehouse1 = warehouse.get();

				if(warehouse1.getStatus_verification() != Constant.StatusVerification.APPROVED.getStatus() ||
						warehouse1.getStatus() == 0)
					return Constant.Code.WAREHOUSE_NOT_ACTIVE.getStatusCode();

				if(warehouseDao.findAllWarehouseFirstClass().size() == 5 && status == 1)
					return Constant.Code.FIRST_CLASS_WAREHOUSE_ALREADY_REACH.getStatusCode();

				warehouse1.setIs_first_class_warehouse(status);
				warehouse1.setUpdated_at(new Date());
				warehouse1.setUpdated_by(jwt.getUser_id());

				warehouseDao.save(warehouse1);

				return Constant.Code.SUCCESS.getStatusCode();
			} else {
				return Constant.Code.WAREHOUSE_NOT_FOUND.getStatusCode();
			}
		} else {
			return Constant.Code.ROLE_NOT_ALLOWED.getStatusCode();
		}
	}
}
