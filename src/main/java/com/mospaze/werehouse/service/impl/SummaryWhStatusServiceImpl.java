package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.SummaryWhStatusNew;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.SummaryWhStatusNewModel;
import com.mospaze.werehouse.model.UploadCsvModel;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.SummaryWhStatusNewDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.SummaryWhStatusService;
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
 * @see SummaryWhStatusService
 */
@Service
public class SummaryWhStatusServiceImpl implements SummaryWhStatusService {

    @Autowired
    private Util util;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private SummaryWhStatusNewDao summaryWhStatusNewDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Override
    @Transactional
    public GeneralResponse uploadSummaryWhStatus(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException {
        List<SummaryWhStatusNewModel> summaryWhStatusNewModels = util.read(SummaryWhStatusNewModel.class, file.getInputStream());
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

        for(SummaryWhStatusNewModel summaryWhStatusNewModel : summaryWhStatusNewModels){
            try{
                SummaryWhStatusNew summaryWhStatusNew = new SummaryWhStatusNew();

                summaryWhStatusNew.setUser_id(buyer_id);

                summaryWhStatusNew.setDate(Constant.SIMPLE_DATE_FORMAT_5.parse(summaryWhStatusNewModel.getDate()));

                summaryWhStatusNew.setInbound_in_plt(Integer.parseInt(summaryWhStatusNewModel.getInbound_in_plt()));
                summaryWhStatusNew.setOutbound_in_plt(Integer.parseInt(summaryWhStatusNewModel.getOutbound_in_plt()));
                summaryWhStatusNew.setThroughput_in_plt(Integer.parseInt(summaryWhStatusNewModel.getThroughput_in_plt()));
                summaryWhStatusNew.setInbound_in_case(Integer.parseInt(summaryWhStatusNewModel.getInbound_in_case()));
                summaryWhStatusNew.setOutbound_in_case(Integer.parseInt(summaryWhStatusNewModel.getOutbound_in_case()));
                summaryWhStatusNew.setThroughput_in_case(Integer.parseInt(summaryWhStatusNewModel.getThroughput_in_case()));
                summaryWhStatusNew.setOccupancy(Double.parseDouble(summaryWhStatusNewModel.getOccupancy()));

                Optional<Warehouse> warehouse = warehouseDao.findByWarehouseName(summaryWhStatusNewModel.getWarehouse().trim());

                if(!warehouse.isPresent()) {
                    skipped.add(summaryWhStatusNewModel);

                    continue;
                }

                summaryWhStatusNew.setWarehouse(warehouse.get().getId());

                Optional<SummaryWhStatusNew> summaryWhStatusNew1 = summaryWhStatusNewDao.validateDuplicateSummaryWhStatus(summaryWhStatusNew.getUser_id(),
                        summaryWhStatusNew.getWarehouse(),
                        Constant.SIMPLE_DATE_FORMAT_5.format(summaryWhStatusNew.getDate()));

                if(summaryWhStatusNew1.isPresent())
                    summaryWhStatusNewDao.delete(summaryWhStatusNew1.get());

                summaryWhStatusNew.setCreated_at(new Date());
                summaryWhStatusNew.setCreated_by(jwt.getUser_id());

                SummaryWhStatusNew whStatusNew = summaryWhStatusNewDao.save(summaryWhStatusNew);

                if (whStatusNew.getId() > 0)
                    success++;
            }catch (Exception e){
                skipped.add(summaryWhStatusNewModel);
            }

        }

        generalResponse.setData(UploadCsvModel.builder().skipped(skipped).success(success).build());
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        return generalResponse;
    }
}
