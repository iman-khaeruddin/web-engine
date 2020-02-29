package com.mospaze.werehouse.service.impl;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseSpaceService;
import com.mospaze.werehouse.repository.WarehouseSpaceServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseSpaceService;
import com.mospaze.werehouse.repository.MsWarehouseSpaceServiceDao;
import com.mospaze.werehouse.service.MsWarehouseSpaceServiceService;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseSpaceServiceService
 */
@Service
public class MsWarehouseSpaceServiceServiceImpl implements MsWarehouseSpaceServiceService{

	@Autowired
	private MsWarehouseSpaceServiceDao msWarehouseSpaceServiceDao;

	@Autowired
	private WarehouseSpaceServiceDao warehouseSpaceServiceDao;
	
	@Override
	public List<WarehouseSpaceService> getMsWarehouseSpaceServicesById(int id) {
		return msWarehouseSpaceServiceDao.getMsWarehouseSpaceServicesById(id);
	}

	@Override
	public Iterable<MsWarehouseSpaceService> getMasterWarehouseSpaceService() {
		return msWarehouseSpaceServiceDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseSpaceService save(com.mospaze.werehouse.entity.WarehouseSpaceService warehouseSpaceService) {
		return warehouseSpaceServiceDao.save(warehouseSpaceService);
	}

	@Override
	public void delete(int warehouse_id, int space_service_id) {
		warehouseSpaceServiceDao.delete(warehouse_id, space_service_id);
	}

	@Override
	public void update(int warehouse_id, int space_service_id) {
		warehouseSpaceServiceDao.update(warehouse_id, space_service_id);
	}

	@Override
	public int countBySpaceServiceId(int warehouse_id, int space_service_id) {
		return warehouseSpaceServiceDao.countBySpaceServiceId(warehouse_id, space_service_id);
	}
}
