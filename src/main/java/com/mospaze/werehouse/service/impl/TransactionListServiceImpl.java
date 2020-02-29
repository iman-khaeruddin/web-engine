package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.TransactionListNew;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.TransactionListModel;
import com.mospaze.werehouse.model.UploadCsvModel;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.TransactionListNewDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.TransactionListService;
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
 * @see com.mospaze.werehouse.service.TransactionListService
 */
@Service
public class TransactionListServiceImpl implements TransactionListService {

    @Autowired
    private Util util;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private TransactionListNewDao transactionListNewDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Override
    @Transactional
    public GeneralResponse uploadTransactionList(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException {
        List<TransactionListModel> transactionListModels = util.read(TransactionListModel.class, file.getInputStream());
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

        if(transactionListModels.size() > 0)
            transactionListNewDao.deleteAll();

        for(TransactionListModel transactionListModel : transactionListModels){
            try {
                TransactionListNew transactionListNew = new TransactionListNew();

                transactionListNew.setUser_id(buyer_id);

                //qty
                transactionListNew.setQty(Integer.parseInt(transactionListModel.getQty()));

                transactionListNew.setCreate_date(Constant.SIMPLE_DATE_FORMAT_5.parse(transactionListModel.getCreate_date()));

                //status
                if (Constant.StatusTransactionList().contains(transactionListModel.getStatus().toUpperCase().trim())) {
                    transactionListNew.setStatus(transactionListModel.getStatus().toUpperCase().trim());
                    if(transactionListNew.getStatus().trim().equalsIgnoreCase("closed"))
                        transactionListNew.setClose_date(Constant.SIMPLE_DATE_FORMAT_5.parse(transactionListModel.getClose_date()));
                } else {
                    skipped.add(transactionListModel);

                    continue;
                }

                Optional<Warehouse> warehouse = warehouseDao.findByWarehouseName(transactionListModel.getWarehouse().trim());

                if(!warehouse.isPresent()) {
                    skipped.add(transactionListModel);

                    continue;
                }

                //warehouse
                transactionListNew.setWarehouse(warehouse.get().getId());

                //sku
                if(transactionListModel.getSku() != null && !transactionListModel.getSku().isEmpty())
                    transactionListNew.setSku(transactionListModel.getSku());

                //job_no
                if(transactionListModel.getJob_no() != null && !transactionListModel.getJob_no().isEmpty())
                    transactionListNew.setJob_no(transactionListModel.getJob_no());

                //location
                if(transactionListModel.getLocation() != null && !transactionListModel.getLocation().isEmpty())
                    transactionListNew.setLocation(transactionListModel.getLocation());

                //batch
                if(transactionListModel.getBatch() != null && !transactionListModel.getBatch().isEmpty())
                    transactionListNew.setBatch(transactionListModel.getBatch());

                Optional<TransactionListNew> transactionListNew1 = transactionListNewDao.validateDuplicateTransactionList(transactionListNew.getUser_id(),
                        transactionListNew.getWarehouse(),
                        transactionListNew.getSku(),
                        transactionListNew.getJob_no(),
                        Constant.SIMPLE_DATE_FORMAT_5.format(transactionListNew.getCreate_date()),
                        transactionListNew.getLocation(),
                        transactionListNew.getBatch());

                if(transactionListNew1.isPresent())
                    transactionListNewDao.delete(transactionListNew1.get());

                //from_to
                if(transactionListModel.getFrom_to() != null && !transactionListModel.getFrom_to().isEmpty())
                    transactionListNew.setFrom_to(transactionListModel.getFrom_to());

                //type
                if(transactionListModel.getType() != null && !transactionListModel.getType().isEmpty())
                    transactionListNew.setType(transactionListModel.getType());

                //category
                if(transactionListModel.getCategory() != null && !transactionListModel.getCategory().isEmpty())
                    transactionListNew.setCategory(transactionListModel.getCategory());

                //packing
                if(transactionListModel.getPacking() != null && !transactionListModel.getPacking().isEmpty())
                    transactionListNew.setPacking(transactionListModel.getPacking());

                //transport_type
                if(transactionListModel.getTransport_type() != null && !transactionListModel.getTransport_type().isEmpty())
                    transactionListNew.setTransport_type(transactionListModel.getTransport_type());

                transactionListNew.setCreated_at(new Date());
                transactionListNew.setCreated_by(jwt.getUser_id());

                TransactionListNew transactionListNew2 = transactionListNewDao.save(transactionListNew);

                if (transactionListNew2.getId() > 0)
                    success++;
            }catch (Exception e){
                skipped.add(transactionListModel);
            }
        }

        generalResponse.setData(UploadCsvModel.builder().skipped(skipped).success(success).build());
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        return generalResponse;
    }
}
