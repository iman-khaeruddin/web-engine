package com.mospaze.werehouse.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseCategory;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseCategory;
import com.mospaze.werehouse.repository.MsWarehouseCategoryDao;
import com.mospaze.werehouse.service.MsWarehouseCategoryService;

import javax.transaction.Transactional;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseCategoryService
 */
@Service
public class MsWarehouseCategoryServiceImpl implements MsWarehouseCategoryService{

	@Autowired
	private MsWarehouseCategoryDao msWarehouseCategoryDao;

	@Autowired
	private WarehouseCategoryDao warehouseCategoryDao;

	@Autowired
	private ModelHasRoleDao modelHasRoleDao;

	@Autowired
	private UsersDao usersDao;
	
	@Override
	public List<WarehouseCategory> getMsWarehouseCategoriesById(int id) {
		return msWarehouseCategoryDao.getMsWarehouseCategoriesById(id);
	}

	@Override
	public Iterable<MsWarehouseCategory> getMasterWarehouseCategory() {
		return msWarehouseCategoryDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseCategory save(com.mospaze.werehouse.entity.WarehouseCategory warehouseCategory) {
		return warehouseCategoryDao.save(warehouseCategory);
	}

	@Override
	public void delete(int warehouse_id, int category_id) {
		warehouseCategoryDao.delete(warehouse_id, category_id);
	}

	@Override
	public int countByCategoryId(int warehouse_id, int category_id) {
		return warehouseCategoryDao.countByCategoryId(warehouse_id, category_id);
	}

	@Override
	public GeneralResponseNew getMasterCategory(Integer page, Integer size, String search_value) {
		GeneralResponseNew generalResponseNew = new GeneralResponseNew();
		Pagination pagination = new Pagination();

		Pageable pageable = PageRequest.of(page-1, size);

		Page<MsWarehouseCategory> msWarehouseCategories = null;

		if (search_value != null)
			msWarehouseCategories = msWarehouseCategoryDao.getMsWarehouseTypesByCategoryName(search_value, pageable);
		else
			msWarehouseCategories = msWarehouseCategoryDao.findAll(pageable);

		generalResponseNew.setResult(msWarehouseCategories.getContent());
		pagination.setLast_page(msWarehouseCategories.getTotalPages());
		pagination.setTotal((int) msWarehouseCategories.getTotalElements());
		pagination.setCurrent_page(page);
		pagination.setPer_page(size);

		if(msWarehouseCategories.getContent().size() > 0){
			pagination.setFrom(((page-1) * size) + 1);
			pagination.setTo(((page-1) * msWarehouseCategories.getContent().size()) + msWarehouseCategories.getContent().size());

			if(msWarehouseCategories.getContent().size() != size)
				pagination.setTo(pagination.getFrom() + msWarehouseCategories.getContent().size() - 1);
		}

		generalResponseNew.setPagination(pagination);

		return generalResponseNew;
	}

	@Override
	public GeneralResponse getMasterCategoryById(int id) {
		GeneralResponse generalResponse = new GeneralResponse();
		Optional<MsWarehouseCategory> msWarehouseCategory = msWarehouseCategoryDao.findById(id);
		if(msWarehouseCategory.isPresent()){
			generalResponse.setData(msWarehouseCategory.get());
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
	public String updateMasterCategory(MsWarehouseCategory msWarehouseCategory) {
		Optional<MsWarehouseCategory> msWarehouseCategory1 = msWarehouseCategoryDao.findById(msWarehouseCategory.getId());
		if(msWarehouseCategory1.isPresent()){
			MsWarehouseCategory msWarehouseCategory2 = msWarehouseCategory1.get();
			msWarehouseCategory2.setStatus(msWarehouseCategory.getStatus());
			msWarehouseCategory2.setCategory_name(msWarehouseCategory.getCategory_name());
			msWarehouseCategory2.setUpdated_at(new Date());
			msWarehouseCategoryDao.save(msWarehouseCategory2);

			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}

	@Override
	@Transactional
	public String insertMasterCategory(MsWarehouseCategory msWarehouseCategory) {
		msWarehouseCategory.setCreated_at(new Date());
		msWarehouseCategory.setUpdated_at(new Date());
		msWarehouseCategoryDao.save(msWarehouseCategory);
		return Constant.Code.SUCCESS.getStatusCode();
	}

	@Override
	@Transactional
	public String deleteMasterCategory(int id) {
		Optional<MsWarehouseCategory> msWarehouseCategory = msWarehouseCategoryDao.findById(id);
		if(msWarehouseCategory.isPresent()){
			msWarehouseCategoryDao.deleteById(id);
			return Constant.Code.SUCCESS.getStatusCode();
		} else {
			return Constant.Code.DATA_NOT_FOUND.getStatusCode();
		}
	}
}
