package com.mospaze.werehouse.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseAutomation;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseAutomationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseAutomation;
import com.mospaze.werehouse.repository.MsWarehouseAutomationDao;
import com.mospaze.werehouse.service.MsWarehouseAutomationService;

import javax.transaction.Transactional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseAutomationService
 */
@Service
public class MsWarehouseAutomationServiceImpl implements MsWarehouseAutomationService{

	@Autowired
	private MsWarehouseAutomationDao msWarehouseAutomationDao;

	@Autowired
	private WarehouseAutomationDao warehouseAutomationDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private UsersDao usersDao;
	
	@Override
	public List<WarehouseAutomation> getMsWarehouseAutomationsById(int id) {
		return msWarehouseAutomationDao.getMsWarehouseAutomationsById(id);
	}

	@Override
	public Iterable<MsWarehouseAutomation> getMasterWarehouseAutomation() {
		return msWarehouseAutomationDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseAutomation save(com.mospaze.werehouse.entity.WarehouseAutomation warehouseAutomation) {
		return warehouseAutomationDao.save(warehouseAutomation);
	}

	@Override
	public void delete(int warehouse_id) {
		warehouseAutomationDao.delete(warehouse_id);
	}

	@Override
	public void update(int warehouse_id, int automation_id) {
		warehouseAutomationDao.update(warehouse_id, automation_id);
	}

	@Override
	@Deprecated
	public int countByAutomationId(int warehouse_id, int automation_id) {
		return warehouseAutomationDao.countByAutomationId(warehouse_id, automation_id);
	}

	@Override
	public GeneralResponseNew getMasterAutomation(Integer page, Integer size, String search_value) {
		GeneralResponseNew generalResponseNew = new GeneralResponseNew();
		Pagination pagination = new Pagination();

		Pageable pageable = PageRequest.of(page-1, size);

		Page<MsWarehouseAutomation> msWarehouseAutomations = null;

		if(search_value != null)
			msWarehouseAutomations = msWarehouseAutomationDao.getMsWarehouseAutomationsByAutomationName(search_value, pageable);
		else
			msWarehouseAutomations = msWarehouseAutomationDao.findAll(pageable);

		generalResponseNew.setResult(msWarehouseAutomations.getContent());
		pagination.setLast_page(msWarehouseAutomations.getTotalPages());
		pagination.setTotal((int) msWarehouseAutomations.getTotalElements());
		pagination.setCurrent_page(page);
		pagination.setPer_page(size);

		if(msWarehouseAutomations.getContent().size() > 0){
			pagination.setFrom(((page-1) * size) + 1);
			pagination.setTo(((page-1) * msWarehouseAutomations.getContent().size()) + msWarehouseAutomations.getContent().size());

			if(msWarehouseAutomations.getContent().size() != size)
				pagination.setTo(pagination.getFrom() + msWarehouseAutomations.getContent().size() - 1);
		}

		generalResponseNew.setPagination(pagination);

		return generalResponseNew;
	}

	@Override
	public GeneralResponse getMasterAutomationById(int id) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<MsWarehouseAutomation> msWarehouseAutomation = msWarehouseAutomationDao.findById(id);
		if (msWarehouseAutomation.isPresent()) {
			generalResponse.setData(msWarehouseAutomation.get());
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
	public String updateMasterAutomation(MsWarehouseAutomation msWarehouseAutomation) {
		Optional<MsWarehouseAutomation> msWarehouseAutomation1 = msWarehouseAutomationDao.findById(msWarehouseAutomation.getId());
		if(msWarehouseAutomation1.isPresent()){
			MsWarehouseAutomation msWarehouseAutomation2 = msWarehouseAutomation1.get();
			msWarehouseAutomation2.setStatus(msWarehouseAutomation.getStatus());
			msWarehouseAutomation2.setAutomation_name(msWarehouseAutomation.getAutomation_name());
			msWarehouseAutomation2.setUpdated_at(new Date());
			msWarehouseAutomationDao.save(msWarehouseAutomation2);

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}

	@Override
	@Transactional
	public String insertMasterAutomation(MsWarehouseAutomation msWarehouseAutomation) {
		msWarehouseAutomation.setCreated_at(new Date());
		msWarehouseAutomation.setUpdated_at(new Date());
		msWarehouseAutomationDao.save(msWarehouseAutomation);
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteMasterAutomation(int id) {
		Optional<MsWarehouseAutomation> msWarehouseAutomation = msWarehouseAutomationDao.findById(id);
		if(msWarehouseAutomation.isPresent()){
			msWarehouseAutomationDao.deleteById(id);
			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}
}
