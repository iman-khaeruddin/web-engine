package com.mospaze.werehouse.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseType;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseType;
import com.mospaze.werehouse.repository.MsWarehouseTypeDao;
import com.mospaze.werehouse.service.MsWarehouseTypeService;

import javax.transaction.Transactional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseTypeService
 */
@Service
public class MsWarehouseTypeServiceImpl implements MsWarehouseTypeService{

	@Autowired
	private MsWarehouseTypeDao msWarehouseTypeDao;

	@Autowired
	private WarehouseTypeDao warehouseTypeDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private UsersDao usersDao;

	@Override
	public List<WarehouseType> getMsWarehouseTypesById(int id) {
		return msWarehouseTypeDao.getMsWarehouseTypesById(id);
	}

	@Override
	public Iterable<MsWarehouseType> getMasterWarehouseType() {
		return msWarehouseTypeDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseType save(com.mospaze.werehouse.entity.WarehouseType warehouseType) {
		return warehouseTypeDao.save(warehouseType);
	}

	@Override
	public void delete(int warehouse_id) {
		warehouseTypeDao.delete(warehouse_id);
	}

	@Override
	public void update(int warehouse_id, int types_id) {
		warehouseTypeDao.update(warehouse_id, types_id);
	}

	@Override
	@Deprecated
	public int countByTypeId(int warehouse_id, int types_id) {
		return warehouseTypeDao.countByTypeId(warehouse_id, types_id);
	}

	@Override
	public GeneralResponseNew getMasterType(Integer page, Integer size, String search_value) {
		GeneralResponseNew generalResponseNew = new GeneralResponseNew();
		Pagination pagination = new Pagination();

		Pageable pageable = PageRequest.of(page-1, size);

		Page<MsWarehouseType> msWarehouseTypes = null;

		if (search_value != null)
			msWarehouseTypes = msWarehouseTypeDao.getMsWarehouseTypesByTypeName(search_value, pageable);
		else
			msWarehouseTypes = msWarehouseTypeDao.findAll(pageable);

		generalResponseNew.setResult(msWarehouseTypes.getContent());
		pagination.setLast_page(msWarehouseTypes.getTotalPages());
		pagination.setTotal((int) msWarehouseTypes.getTotalElements());
		pagination.setCurrent_page(page);
		pagination.setPer_page(size);

		if(msWarehouseTypes.getContent().size() > 0){
			pagination.setFrom(((page-1) * size) + 1);
			pagination.setTo(((page-1) * msWarehouseTypes.getContent().size()) + msWarehouseTypes.getContent().size());

			if(msWarehouseTypes.getContent().size() != size)
				pagination.setTo(pagination.getFrom() + msWarehouseTypes.getContent().size() - 1);
		}

		generalResponseNew.setPagination(pagination);

		return generalResponseNew;
	}

	@Override
	public GeneralResponse getMasterTypeById(int id) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<MsWarehouseType> msWarehouseType = msWarehouseTypeDao.findById(id);
		if(msWarehouseType.isPresent()){
			generalResponse.setData(msWarehouseType.get());
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
	public String updateMasterType(MsWarehouseType msWarehouseType) {
		Optional<MsWarehouseType> msWarehouseType1 = msWarehouseTypeDao.findById(msWarehouseType.getId());
		if(msWarehouseType1.isPresent()){
			MsWarehouseType msWarehouseType2 = msWarehouseType1.get();
			msWarehouseType2.setStatus(msWarehouseType.getStatus());
			msWarehouseType2.setType_name(msWarehouseType.getType_name());
			msWarehouseType2.setUpdated_at(new Date());
			msWarehouseTypeDao.save(msWarehouseType2);

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}

	@Override
	@Transactional
	public String insertMasterType(MsWarehouseType msWarehouseType) {
		msWarehouseType.setCreated_at(new Date());
		msWarehouseType.setUpdated_at(new Date());
		msWarehouseTypeDao.save(msWarehouseType);
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteMasterType(int id) {
		Optional<MsWarehouseType> msWarehouseType = msWarehouseTypeDao.findById(id);
		if(msWarehouseType.isPresent()){
			msWarehouseTypeDao.deleteById(id);
			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}
}
