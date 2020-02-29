package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.entity.MsWarehouseTemperature;
import com.mospaze.werehouse.entity.projector.WarehouseTemperature;
import com.mospaze.werehouse.repository.MsWarehouseTemperatureDao;
import com.mospaze.werehouse.repository.WarehouseTemperatureDao;
import com.mospaze.werehouse.service.MsWarehouseTemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.MsWarehouseTemperatureService
 */
@Service
public class MsWarehouseTemperatureServiceImpl implements MsWarehouseTemperatureService {

    @Autowired
    private MsWarehouseTemperatureDao msWarehouseTemperatureDao;

    @Autowired
    private WarehouseTemperatureDao warehouseTemperatureDao;

    @Override
    public List<WarehouseTemperature> getMsWarehouseTemperatureById(int id) {
        return msWarehouseTemperatureDao.getMsWarehouseTemperatureById(id);
    }

    @Override
    public Iterable<MsWarehouseTemperature> getMasterWarehouseTemperature() {
        return msWarehouseTemperatureDao.findAllByActiveStatus();
    }

    @Override
    public com.mospaze.werehouse.entity.WarehouseTemperature save(com.mospaze.werehouse.entity.WarehouseTemperature warehouseTemperature) {
        return warehouseTemperatureDao.save(warehouseTemperature);
    }

    @Override
    public void delete(int warehouse_id) {
        warehouseTemperatureDao.delete(warehouse_id);
    }

    @Override
    public void update(int warehouse_id, int temperature_id) {
        warehouseTemperatureDao.update(warehouse_id, temperature_id);
    }

    @Override
    @Deprecated
    public int countByTemperatureId(int warehouse_id, int temperature_id) {
        return warehouseTemperatureDao.countByTemperatureId(warehouse_id, temperature_id);
    }
}
