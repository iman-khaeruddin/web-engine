package com.mospaze.werehouse.service.impl;

import java.util.List;

import com.mospaze.werehouse.entity.MsWarehouseSpace;
import com.mospaze.werehouse.repository.WarehouseServiceDao;
import com.mospaze.werehouse.repository.WarehouseSpaceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mospaze.werehouse.entity.projector.WarehouseSpace;
import com.mospaze.werehouse.repository.MsWarehouseSpaceDao;
import com.mospaze.werehouse.service.MsWarehouseSpaceService;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseSpaceService
 */
@Service
public class MsWarehouseSpaceServiceImpl implements MsWarehouseSpaceService{

	@Autowired
	private MsWarehouseSpaceDao msWarehouseSpaceDao;

	@Autowired
	private WarehouseSpaceDao warehouseSpaceDao;
	
	@Override
	public List<WarehouseSpace> getMsWarehouseSpacesById(int id) {
		return msWarehouseSpaceDao.getMsWarehouseSpacesById(id);
	}

	@Override
	public Iterable<MsWarehouseSpace> getMasterWarehouseSpace() {
		return msWarehouseSpaceDao.findAllByActiveStatus();
	}

	@Override
	public com.mospaze.werehouse.entity.WarehouseSpace save(com.mospaze.werehouse.entity.WarehouseSpace warehouseSpace) {
		return warehouseSpaceDao.save(warehouseSpace);
	}

	@Override
	public void delete(int warehouse_id, int space_id) {
		warehouseSpaceDao.delete(warehouse_id, space_id);
	}

	@Override
	public int countBySpaceId(int warehouse_id, int space_id) {
		return warehouseSpaceDao.countBySpaceId(warehouse_id, space_id);
	}
}
