package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.entity.MsWarehouseOperatingHour;
import com.mospaze.werehouse.entity.WarehouseOperatingHour;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.MasterDataPageable;
import com.mospaze.werehouse.model.Pagination;
import com.mospaze.werehouse.repository.MsWarehouseOperatingHourDao;
import com.mospaze.werehouse.repository.WarehouseOperatingHourDao;
import com.mospaze.werehouse.service.MsWarehouseOperatingHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 17/12/2019
 * @see com.mospaze.werehouse.service.MsWarehouseOperatingHourService
 */
@Service
public class MsWarehouseOperatingHourServiceImpl implements MsWarehouseOperatingHourService {

    @Autowired
    private MsWarehouseOperatingHourDao msWarehouseOperatingHourDao;

    @Autowired
    private WarehouseOperatingHourDao warehouseOperatingHourDao;

    @Override
    public WarehouseOperatingHour save(WarehouseOperatingHour warehouseOperatingHour) {
        return warehouseOperatingHourDao.save(warehouseOperatingHour);
    }

    @Override
    public List<com.mospaze.werehouse.entity.projector.WarehouseOperatingHour> getMsWarehouseOperationHourById(int id) {
        return msWarehouseOperatingHourDao.getMsWarehouseOperatingHourById(id);
    }

    @Override
    public int countByOperationHourId(int warehouse_id, int operation_hour_id) {
        return warehouseOperatingHourDao.countByOperationHourId(warehouse_id, operation_hour_id);
    }

    @Override
    public void delete(int warehouse_id, int operation_hour_id) {
        warehouseOperatingHourDao.delete(warehouse_id, operation_hour_id);
    }

    @Override
    public GeneralResponseNew getMasterOperatingHour(Integer page, Integer size, String search_value) {
        GeneralResponseNew generalResponseNew = new GeneralResponseNew();
        Pagination pagination = new Pagination();

        Pageable pageable = PageRequest.of(page-1, size);

        Page<MsWarehouseOperatingHour> msWarehouseOperatingHours = null;

        if (search_value != null)
            msWarehouseOperatingHours = msWarehouseOperatingHourDao.getMsWarehouseOperatingByOperationName(search_value, pageable);
        else
            msWarehouseOperatingHours = msWarehouseOperatingHourDao.findAll(pageable);

        generalResponseNew.setResult(msWarehouseOperatingHours.getContent());
        pagination.setLast_page(msWarehouseOperatingHours.getTotalPages());
        pagination.setTotal((int) msWarehouseOperatingHours.getTotalElements());
        pagination.setCurrent_page(page);
        pagination.setPer_page(size);

        if(msWarehouseOperatingHours.getContent().size() > 0){
            pagination.setFrom(((page-1) * size) + 1);
            pagination.setTo(((page-1) * msWarehouseOperatingHours.getContent().size()) + msWarehouseOperatingHours.getContent().size());

            if(msWarehouseOperatingHours.getContent().size() != size)
                pagination.setTo(pagination.getFrom() + msWarehouseOperatingHours.getContent().size() - 1);
        }

        generalResponseNew.setPagination(pagination);

        return generalResponseNew;
    }

    @Override
    public Iterable<MsWarehouseOperatingHour> getMasterWarehouseOperatingHour() {
        return msWarehouseOperatingHourDao.findAll();
    }

    @Override
    public GeneralResponse getMasterOperatingHourById(int id) {
        GeneralResponse generalResponse = new GeneralResponse();
        Optional<MsWarehouseOperatingHour> msWarehouseOperatingHour = msWarehouseOperatingHourDao.findById(id);
        if(msWarehouseOperatingHour.isPresent()){
            generalResponse.setData(msWarehouseOperatingHour.get());
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
    public String updateMasterOperatingHour(MsWarehouseOperatingHour msWarehouseOperatingHour) {
        Optional<MsWarehouseOperatingHour> msWarehouseOperatingHour1 = msWarehouseOperatingHourDao.findById(msWarehouseOperatingHour.getId());
        if(msWarehouseOperatingHour1.isPresent()){
            MsWarehouseOperatingHour msWarehouseOperatingHour2 = msWarehouseOperatingHour1.get();
            msWarehouseOperatingHour2.setStatus(msWarehouseOperatingHour.getStatus());
            msWarehouseOperatingHour2.setOperation_hour_name(msWarehouseOperatingHour.getOperation_hour_name());
            msWarehouseOperatingHour2.setUpdate_at(new Date());
            msWarehouseOperatingHourDao.save(msWarehouseOperatingHour2);

            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }

    @Override
    @Transactional
    public String insertMasterOperatingHour(MsWarehouseOperatingHour msWarehouseOperatingHour) {
        msWarehouseOperatingHour.setCreated_at(new Date());
        msWarehouseOperatingHour.setUpdate_at(new Date());
        msWarehouseOperatingHourDao.save(msWarehouseOperatingHour);
        return Constant.Code.SUCCESS.getStatusCode();
    }

    @Override
    @Transactional
    public String deleteMasterOperatingHour(int id) {
        Optional<MsWarehouseOperatingHour> msWarehouseCategory = msWarehouseOperatingHourDao.findById(id);
        if(msWarehouseCategory.isPresent()){
            msWarehouseOperatingHourDao.deleteById(id);
            return Constant.Code.SUCCESS.getStatusCode();
        } else {
            return Constant.Code.DATA_NOT_FOUND.getStatusCode();
        }
    }
}
