package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.StockOnHandNew;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.StockOnHandNewModel;
import com.mospaze.werehouse.model.UploadCsvModel;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.StockOnHandNewDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.StockOnHandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author Iman Khaeruddin
 * @since 07/12/2019
 * @see com.mospaze.werehouse.service.StockOnHandService
 */
@Service
public class StockOnHandServiceImpl implements StockOnHandService {

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private Util util;

    @Autowired
    private StockOnHandNewDao stockOnHandNewDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Override
    @Transactional
    public GeneralResponse uploadStockOnHand(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException {
        List<StockOnHandNewModel> stockOnHandNewModels = util.read(StockOnHandNewModel.class, file.getInputStream());
        List<Object> skipped = new ArrayList<>();
        Integer success = 0;

        GeneralResponse generalResponse = new GeneralResponse();

        if(jwt.getRole_id()!= Constant.Role.SUPER_ADMIN.getRole() &&
                jwt.getRole_id()!=Constant.Role.ADMIN.getRole()) {
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        Optional<ModelHasRole> role_buyer_id = modelHasRoleDao.findById(buyer_id);

        if(!role_buyer_id.isPresent()){
            generalResponse.setCode(Constant.Code.USER_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        if(role_buyer_id.get().getRole_id()!= Constant.Role.BUYER.getRole()) {
            generalResponse.setCode(Constant.Code.NOT_BUYER.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        for(StockOnHandNewModel stockOnHandNewModel : stockOnHandNewModels){
            try {
                StockOnHandNew stockOnHandNew = new StockOnHandNew();

                stockOnHandNew.setUser_id(buyer_id);

                stockOnHandNew.setReceive_date(Constant.SIMPLE_DATE_FORMAT_5.parse(stockOnHandNewModel.getReceive_date()));
                stockOnHandNew.setExpire_date(Constant.SIMPLE_DATE_FORMAT_5.parse(stockOnHandNewModel.getExpire_date()));
                stockOnHandNew.setDate(Constant.SIMPLE_DATE_FORMAT_5.parse(stockOnHandNewModel.getDate()));

                stockOnHandNew.setQty_release(Integer.parseInt(stockOnHandNewModel.getQty_release()));
                stockOnHandNew.setQty_outbound(Integer.parseInt(stockOnHandNewModel.getQty_outbound()));
                stockOnHandNew.setQty_stock(Integer.parseInt(stockOnHandNewModel.getQty_stock()));

                Optional<Warehouse> warehouse = warehouseDao.findByWarehouseName(stockOnHandNewModel.getWarehouse().trim());

                if(!warehouse.isPresent()) {
                    skipped.add(stockOnHandNewModel);

                    continue;
                }

                stockOnHandNew.setWarehouse(warehouse.get().getId());

                if(stockOnHandNewModel.getPallet_num() != null & !stockOnHandNewModel.getPallet_num().isEmpty())
                    stockOnHandNew.setPallet_num(stockOnHandNewModel.getPallet_num());

                if(stockOnHandNewModel.getSku() != null & !stockOnHandNewModel.getSku().isEmpty())
                    stockOnHandNew.setSku(stockOnHandNewModel.getSku());

                if(stockOnHandNewModel.getPrimary_key() != null & !stockOnHandNewModel.getPrimary_key().isEmpty())
                    stockOnHandNew.setPrimary_key(stockOnHandNewModel.getPrimary_key());

                Optional<StockOnHandNew> stockOnHandNew1 = stockOnHandNewDao.validateDuplicateStockOnHand(stockOnHandNew.getUser_id(),
                        stockOnHandNew.getWarehouse(),
                        Constant.SIMPLE_DATE_FORMAT_5.format(stockOnHandNew.getReceive_date()),
                        stockOnHandNew.getPallet_num(),
                        stockOnHandNew.getSku(),
                        stockOnHandNew.getPrimary_key(),
                        Constant.SIMPLE_DATE_FORMAT_5.format(stockOnHandNew.getDate()));

                if(stockOnHandNew1.isPresent())
                    stockOnHandNewDao.delete(stockOnHandNew1.get());

                if(stockOnHandNewModel.getLocation() != null & !stockOnHandNewModel.getLocation().isEmpty())
                    stockOnHandNew.setLocation(stockOnHandNewModel.getLocation());

                if(stockOnHandNewModel.getDescription() != null & !stockOnHandNewModel.getDescription().isEmpty())
                    stockOnHandNew.setDescription(stockOnHandNewModel.getDescription());

                if(stockOnHandNewModel.getCategory() != null & !stockOnHandNewModel.getCategory().isEmpty())
                    stockOnHandNew.setCategory(stockOnHandNewModel.getCategory());

                if(stockOnHandNewModel.getRecord_num() != null & !stockOnHandNewModel.getRecord_num().isEmpty())
                    stockOnHandNew.setRecord_num(stockOnHandNewModel.getRecord_num());

                if(stockOnHandNewModel.getBatch() != null & !stockOnHandNewModel.getBatch().isEmpty())
                    stockOnHandNew.setBatch(stockOnHandNewModel.getBatch());

                if(stockOnHandNewModel.getStatus_stock() != null & !stockOnHandNewModel.getStatus_stock().isEmpty())
                    stockOnHandNew.setStatus_stock(stockOnHandNewModel.getStatus_stock());

                if(stockOnHandNewModel.getPallet() != null & !stockOnHandNewModel.getPallet().isEmpty())
                    stockOnHandNew.setPallet(stockOnHandNewModel.getPallet());

                if(stockOnHandNewModel.getKeterangan() != null & !stockOnHandNewModel.getKeterangan().isEmpty())
                    stockOnHandNew.setKeterangan(stockOnHandNewModel.getKeterangan());

                stockOnHandNew.setCreated_at(new Date());
                stockOnHandNew.setCreated_by(jwt.getUser_id());

                StockOnHandNew stockOnHandNew2 = stockOnHandNewDao.save(stockOnHandNew);

                if(stockOnHandNew2.getId() > 0)
                    success++;
            }catch (Exception e){
                skipped.add(stockOnHandNewModel);
            }
        }

        generalResponse.setData(UploadCsvModel.builder().skipped(skipped).success(success).build());
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        return generalResponse;
    }
}
