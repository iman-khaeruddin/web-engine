package com.mospaze.werehouse.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseFeature;
import com.mospaze.werehouse.entity.WarehouseFeatures;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseFeaturesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseFeature;
import com.mospaze.werehouse.repository.MsWarehouseFeatureDao;
import com.mospaze.werehouse.service.MsWarehouseFeatureService;

import javax.transaction.Transactional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseFeatureService
 */
@Service
public class MsWarehouseFeatureServiceImpl implements MsWarehouseFeatureService{

	@Autowired
	private MsWarehouseFeatureDao msWarehouseFeatureDao;

	@Autowired
	private WarehouseFeaturesDao warehouseFeaturesDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private UsersDao usersDao;
	
	@Override
	public List<WarehouseFeature> getMsWarehouseFeaturesById(int id) {
		return msWarehouseFeatureDao.getMsWarehouseFeaturesById(id);
	}

	@Override
	public Iterable<MsWarehouseFeature> getMasterWarehouseFeature() {
		return msWarehouseFeatureDao.findAllByActiveStatus();
	}

	@Override
	public WarehouseFeatures save(WarehouseFeatures warehouseFeatures) {
		return warehouseFeaturesDao.save(warehouseFeatures);
	}

	@Override
	public void delete(int warehouse_id, int features_id) {
		warehouseFeaturesDao.delete(warehouse_id, features_id);
	}

	@Override
	public int countByFeatureId(int warehouse_id, int features_id) {
		return warehouseFeaturesDao.countByFeatureId(warehouse_id, features_id);
	}

	@Override
	@Transactional
	public String updateMasterFeature(MsWarehouseFeature msWarehouseFeature) {
		Optional<MsWarehouseFeature> msWarehouseFeature1 = msWarehouseFeatureDao.findById(msWarehouseFeature.getId());
		if(msWarehouseFeature1.isPresent()){
			MsWarehouseFeature msWarehouseFeature2 = msWarehouseFeature1.get();
			msWarehouseFeature2.setStatus(msWarehouseFeature.getStatus());
			msWarehouseFeature2.setFeatures_name(msWarehouseFeature.getFeatures_name());
			msWarehouseFeature2.setUpdated_at(new Date());
			msWarehouseFeatureDao.save(msWarehouseFeature2);

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}

	@Override
	public GeneralResponse getMasterFeatureById(int id) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<MsWarehouseFeature> msWarehouseFeature = msWarehouseFeatureDao.findById(id);
		if(msWarehouseFeature.isPresent()){
			generalResponse.setData(msWarehouseFeature.get());
			generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
		} else {
			generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
			generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
			generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
		}
		return generalResponse;
	}

	@Override
	public GeneralResponseNew getMasterFeature(Integer page, Integer size, String search_value) {
		GeneralResponseNew generalResponseNew = new GeneralResponseNew();
		Pagination pagination = new Pagination();

		Pageable pageable = PageRequest.of(page-1, size);

		Page<MsWarehouseFeature> msWarehouseFeatures = null;

		if(search_value != null)
			msWarehouseFeatures = msWarehouseFeatureDao.getMsWarehouseFeaturesByFeatureName(search_value, pageable);
		else
			msWarehouseFeatures = msWarehouseFeatureDao.findAll(pageable);

		generalResponseNew.setResult(msWarehouseFeatures.getContent());
		pagination.setLast_page(msWarehouseFeatures.getTotalPages());
		pagination.setTotal((int) msWarehouseFeatures.getTotalElements());
		pagination.setCurrent_page(page);
		pagination.setPer_page(size);

		if(msWarehouseFeatures.getContent().size() > 0){
			pagination.setFrom(((page - 1) * size) + 1);
			pagination.setTo(((page - 1) * msWarehouseFeatures.getContent().size()) + msWarehouseFeatures.getContent().size());

			if(msWarehouseFeatures.getContent().size() != size)
				pagination.setTo(pagination.getFrom() + msWarehouseFeatures.getContent().size() - 1);
		}

		generalResponseNew.setPagination(pagination);

		return generalResponseNew;
	}

	@Override
	@Transactional
	public String insertMasterFeature(MsWarehouseFeature msWarehouseFeature) {
		msWarehouseFeature.setCreated_at(new Date());
		msWarehouseFeature.setUpdated_at(new Date());
		msWarehouseFeatureDao.save(msWarehouseFeature);
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteMasterFeature(int id) {
		Optional<MsWarehouseFeature> msWarehouseFeature = msWarehouseFeatureDao.findById(id);
		if(msWarehouseFeature.isPresent()){
			msWarehouseFeatureDao.deleteById(id);
			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}

	}
}
