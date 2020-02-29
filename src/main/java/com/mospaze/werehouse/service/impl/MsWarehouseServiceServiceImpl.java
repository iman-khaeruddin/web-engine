package com.mospaze.werehouse.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseService;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseService;
import com.mospaze.werehouse.repository.MsWarehouseServiceDao;
import com.mospaze.werehouse.service.MsWarehouseServiceService;

import javax.transaction.Transactional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseServiceService
 */
@Service
public class MsWarehouseServiceServiceImpl implements MsWarehouseServiceService{
	
	@Autowired
	private MsWarehouseServiceDao msWarehouseServiceDao;

	@Autowired
	private WarehouseServiceDao warehouseServiceDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private UsersDao usersDao;

	@Override
	public List<WarehouseService> getMsWarehouseServicesById(int id) {
		return msWarehouseServiceDao.getMsWarehouseServicesById(id);
	}

	@Override
	public Iterable<MsWarehouseService> getMasterWarehouseService() {
		return msWarehouseServiceDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseService save(com.mospaze.werehouse.entity.WarehouseService warehouseService) {
		return warehouseServiceDao.save(warehouseService);
	}

	@Override
	public void delete(int warehouse_id, int service_id) {
		warehouseServiceDao.delete(warehouse_id, service_id);
	}

	@Override
	public int countByServiceId(int warehouse_id, int service_id) {
		return warehouseServiceDao.countByServiceId(warehouse_id, service_id);
	}

	@Override
	public GeneralResponseNew getMasterService(Integer page, Integer size, String search_value) {
		GeneralResponseNew generalResponseNew = new GeneralResponseNew();
		Pagination pagination = new Pagination();

		Pageable pageable = PageRequest.of(page-1, size);

		Page<MsWarehouseService> msWarehouseServices = null;

		if(search_value != null)
			msWarehouseServices = msWarehouseServiceDao.getMsWarehouseServicesByServicesName(search_value, pageable);
		else
			msWarehouseServices = msWarehouseServiceDao.findAll(pageable);

		generalResponseNew.setResult(msWarehouseServices.getContent());
		pagination.setLast_page(msWarehouseServices.getTotalPages());
		pagination.setTotal((int) msWarehouseServices.getTotalElements());
		pagination.setCurrent_page(page);
		pagination.setPer_page(size);

		if(msWarehouseServices.getContent().size() > 0){
			pagination.setFrom(((page-1) * size) + 1);
			pagination.setTo(((page-1) * msWarehouseServices.getContent().size()) + msWarehouseServices.getContent().size());

			if(msWarehouseServices.getContent().size() != size)
				pagination.setTo(pagination.getFrom() + msWarehouseServices.getContent().size() - 1);
		}

		generalResponseNew.setPagination(pagination);

		return generalResponseNew;
	}

	@Override
	public GeneralResponse getMasterServiceById(int id) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<MsWarehouseService> msWarehouseService = msWarehouseServiceDao.findById(id);
		if(msWarehouseService.isPresent()){
			generalResponse.setData(msWarehouseService.get());
			generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
		} else {
			generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
		}
		return generalResponse;
	}

	@Override
	@Transactional
	public String updateMasterService(MsWarehouseService msWarehouseService) {
		Optional<MsWarehouseService> msWarehouseService1 = msWarehouseServiceDao.findById(msWarehouseService.getId());
		if(msWarehouseService1.isPresent()){
			MsWarehouseService msWarehouseService2 = msWarehouseService1.get();
			msWarehouseService2.setStatus(msWarehouseService.getStatus());
			msWarehouseService2.setServices_name(msWarehouseService.getServices_name());
			msWarehouseService2.setUpdated_at(new Date());
			msWarehouseServiceDao.save(msWarehouseService2);

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}

	@Override
	@Transactional
	public String insertMasterService(MsWarehouseService msWarehouseService) {
		msWarehouseService.setCreated_at(new Date());
		msWarehouseService.setUpdated_at(new Date());
		msWarehouseServiceDao.save(msWarehouseService);
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteMasterService(int id) {
		Optional<MsWarehouseService> msWarehouseService = msWarehouseServiceDao.findById(id);
		if(msWarehouseService.isPresent()){
			msWarehouseServiceDao.deleteById(id);
			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}
}
