package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.entity.projector.StockOnHand;
import com.mospaze.werehouse.entity.projector.WarehouseName;
import com.mospaze.werehouse.model.SummaryWhStatus;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.*;
import com.mospaze.werehouse.service.TrendOccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 * @see com.mospaze.werehouse.service.TrendOccupancyService
 */
@Service
public class TrendOccupancyServiceImpl implements TrendOccupancyService {

    @Autowired
    private TrendOccupancyDao trendOccupancyDao;

    @Autowired
    private SummaryWhStatusDao summaryWhStatusDao;

    @Autowired
    private SummaryWhStatusNewDao summaryWhStatusNewDao;

    @Autowired
    private StockOnHandDao stockOnHandDao;

    @Autowired
    private TransactinListDao transactinListDao;

    @Autowired
    private CustomStockOnHandDao customStockOnHandDao;

    @Autowired
    private CustomTransactionListDao customTransactionListDao;

    @Autowired
    private Util util;

    @Autowired
    private TrendOccupancyNewDao trendOccupancyNewDao;

    @Autowired
    private TransactionListNewDao transactionListNewDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Override
    public GeneralResponse getTrendOccupancy(String month, JWT jwt, String date, int warehouse) throws ParseException {
        GeneralResponse generalResponse = new GeneralResponse();
        List<TrendOccupancyData> trendOccupancyDataList = new ArrayList<>();
        TrendOccupancyData trendOccupancyData = new TrendOccupancyData();

        List<TrendOccupancyNew> trendOccupancyNewList = new ArrayList<>();

        if(jwt.getRole_id() != Constant.Role.BUYER.getRole()){
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        if(month != null && date == null) {
            Constant.SIMPLE_DATE_FORMAT_6.parse(month);
            trendOccupancyNewList = trendOccupancyNewDao.findTrendOccupanciesByMonth(month, jwt.getUser_id(), warehouse);
        } else if(month == null && date != null) {
            Constant.SIMPLE_DATE_FORMAT_5.parse(date);
            trendOccupancyNewList = trendOccupancyNewDao.findTrendOccupanciesByDate(date, jwt.getUser_id(), warehouse);
        } else {
            trendOccupancyNewList = trendOccupancyNewDao.findAll(jwt.getUser_id(), warehouse);
        }

        Optional<Warehouse> warehouses = warehouseDao.findById(warehouse);

        for(TrendOccupancyNew trendOccupancy : trendOccupancyNewList){
            trendOccupancyData = new TrendOccupancyData();
            trendOccupancyData.setOccupancy(trendOccupancy.getOccupancy());
            trendOccupancyData.setOccupied(trendOccupancy.getOccupied());
            trendOccupancyData.setSpace(trendOccupancy.getSpace());
            trendOccupancyData.setTotal_space(trendOccupancy.getTotal_space());
            trendOccupancyData.setDate(Constant.SIMPLE_DATE_FORMAT_5.format(trendOccupancy.getDate()));
            trendOccupancyData.setWarehouse_name(warehouses.isPresent() ? warehouses.get().getName() : null);

            trendOccupancyDataList.add(trendOccupancyData);

        }

        if(trendOccupancyDataList.size() == 0){
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        generalResponse.setData(trendOccupancyDataList);
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

        return generalResponse;
    }

    @Override
    public GeneralResponse getSummaryWhStatus(String month, JWT jwt, String date, int warehouse) throws ParseException {
        GeneralResponse generalResponse = new GeneralResponse();
        SummaryWhStatusNew summaryWhStatusNew = new SummaryWhStatusNew();
        List<SummaryWhStatusNew> summaryWhStatusNewList = new ArrayList<>();
        SummaryWhStatus summaryWhStatus = new SummaryWhStatus();
        List<SummaryWhStatus> summaryWhStatusList = new ArrayList<>();

        if(jwt.getRole_id() != Constant.Role.BUYER.getRole()){
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        if(month != null && date == null) {
            Constant.SIMPLE_DATE_FORMAT_6.parse(month);
            summaryWhStatusNewList = summaryWhStatusNewDao.findSummaryWhStatusByMonth(month, jwt.getUser_id(), warehouse);
        }else if(month == null && date != null) {
            Constant.SIMPLE_DATE_FORMAT_5.parse(date);
            summaryWhStatusNewList = summaryWhStatusNewDao.findSummaryWhStatusByDate(date, jwt.getUser_id(), warehouse);
        }else {
            summaryWhStatusNewList = summaryWhStatusNewDao.findAll(jwt.getUser_id(), warehouse);
        }

        Optional<Warehouse> warehouses = warehouseDao.findById(warehouse);

        for(SummaryWhStatusNew whStatusNew : summaryWhStatusNewList){
            summaryWhStatus = new SummaryWhStatus();
            summaryWhStatus.setOccupancy(whStatusNew.getOccupancy());
            summaryWhStatus.setWarehouse_name(warehouses.isPresent() ? warehouses.get().getName() : null);
            summaryWhStatus.setDate(Constant.SIMPLE_DATE_FORMAT_5.format(whStatusNew.getDate()));
            summaryWhStatus.setInbound_in_plt(whStatusNew.getInbound_in_plt());
            summaryWhStatus.setOutbound_in_plt(whStatusNew.getOutbound_in_plt());
            summaryWhStatus.setThroughput_in_plt(whStatusNew.getThroughput_in_plt());
            summaryWhStatus.setInbound_in_case(whStatusNew.getInbound_in_case());
            summaryWhStatus.setOutbound_in_case(whStatusNew.getOutbound_in_case());
            summaryWhStatus.setThroughput_in_case(whStatusNew.getThroughput_in_case());

            summaryWhStatusList.add(summaryWhStatus);

        }

        if(summaryWhStatusList.size() == 0){
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        generalResponse.setData(summaryWhStatusList);
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

        return generalResponse;
    }

    @Override
    public GeneralResponse getOccupancyProductFamily(String month, JWT jwt, String date, int warehouse, String start_date, String end_date) throws ParseException {
        GeneralResponse generalResponse = new GeneralResponse();
        List<com.mospaze.werehouse.model.StockOnHand> summaryWhStatuses = new ArrayList<>();
        com.mospaze.werehouse.model.StockOnHand stockOnHand1;

        DecimalFormat formatter = new DecimalFormat("#0.00");

        List<Object[]> stockOnHands = new ArrayList<>();
        List<StockOnHand> stockOnHands1 = new ArrayList<>();

        if(jwt.getRole_id() != Constant.Role.BUYER.getRole()){
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        if(month != null)
            Constant.SIMPLE_DATE_FORMAT_6.parse(month);

        if(date != null)
            Constant.SIMPLE_DATE_FORMAT_5.parse(date);

        if(start_date != null && end_date != null){
            Constant.SIMPLE_DATE_FORMAT_5.parse(start_date);
            Constant.SIMPLE_DATE_FORMAT_5.parse(end_date);
        }

        stockOnHands = customStockOnHandDao.findStockOnHandNew(month, date, warehouse, jwt.getUser_id(), start_date, end_date);

        Optional<Warehouse> warehouses = warehouseDao.findById(warehouse);

        for(Object [] objects : stockOnHands){
            StockOnHand stockOnHand = new StockOnHand();
            String jml = String.valueOf(objects[0]);
            stockOnHand.setJml(Integer.valueOf(jml));

            stockOnHand.setProduct_family(String.valueOf(objects[1]));

            String qty = String.valueOf(objects[2]);
            stockOnHand.setQty(Integer.parseInt(qty));
            stockOnHand.setWarehouse_name(warehouses.isPresent() ? warehouses.get().getName() : null);

            stockOnHands1.add(stockOnHand);
        }

        Integer total_qty = stockOnHands1.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(StockOnHand stockOnHand : stockOnHands1){
            stockOnHand1 = new com.mospaze.werehouse.model.StockOnHand();

            stockOnHand1.setProduct_family(stockOnHand.getProduct_family());
            stockOnHand1.setQty(stockOnHand.getQty());
            stockOnHand1.setPercentage(String.valueOf(formatter.format((stockOnHand1.getQty() / total_qty.doubleValue()) * 100)));
            stockOnHand1.setTotal_qty(total_qty);
            stockOnHand1.setWarehouse_name(stockOnHand.getWarehouse_name());

            summaryWhStatuses.add(stockOnHand1);

        }

        if(summaryWhStatuses.size() == 0){
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        generalResponse.setData(summaryWhStatuses);
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

        return generalResponse;
    }

    @Override
    public GeneralResponse getJobList(int warehouse, JWT jwt, int page, int size, String status) {
        GeneralResponse generalResponse = new GeneralResponse();
        TrendOccupancyDataResponse trendOccupancyDataResponse = new TrendOccupancyDataResponse();
        List<TransactionListNew> transactionLists1 = new ArrayList<>();

        if(jwt.getRole_id() != Constant.Role.BUYER.getRole()){
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        transactionLists1 = customTransactionListDao.findJobListNew(jwt.getUser_id(), warehouse, status, page, size);

        trendOccupancyDataResponse.setJob_list(transactionLists1);

        if(trendOccupancyDataResponse.getJob_list().size() == 0){
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        trendOccupancyDataResponse.setTotal_page(customTransactionListDao.getTotalPageNew(jwt.getUser_id(), warehouse, status, page, size));
        trendOccupancyDataResponse.setTotal_element(customTransactionListDao.getTotalElementNew(jwt.getUser_id(), warehouse, status, page, size));

        if(trendOccupancyDataResponse.getJob_list().size() > 0){
            trendOccupancyDataResponse.setFrom((page * size) + 1);
            trendOccupancyDataResponse.setTo((page * trendOccupancyDataResponse.getJob_list().size()) + trendOccupancyDataResponse.getJob_list().size());

            if(trendOccupancyDataResponse.getJob_list().size() != size)
                trendOccupancyDataResponse.setTo(trendOccupancyDataResponse.getFrom() + trendOccupancyDataResponse.getJob_list().size() - 1);
        }

        generalResponse.setData(trendOccupancyDataResponse);
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

        return generalResponse;
    }

    @Override
    public GeneralResponse getStockStatus(int warehouse, JWT jwt) {
        GeneralResponse generalResponse = new GeneralResponse();
        List<com.mospaze.werehouse.entity.projector.TransactionList> transactionLists = new ArrayList<>();

        if(jwt.getRole_id() != Constant.Role.BUYER.getRole()){
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        transactionLists = transactionListNewDao.stockStatusWarehouse(warehouse, jwt.getUser_id());

        if(transactionLists.size() == 0){
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        generalResponse.setData(transactionLists);
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

        return generalResponse;
    }

    @Override
    @Transactional
    public GeneralResponse uploadTrendOccupancy(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException {
        List<TrendOccupancyNewModel> trendOccupancyNewModels = util.read(TrendOccupancyNewModel.class, file.getInputStream());
        List<Object> skipped = new ArrayList<>();
        Integer success = 0;

        GeneralResponse generalResponse = new GeneralResponse();

        if(jwt.getRole_id()!=Constant.Role.SUPER_ADMIN.getRole() &&
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

        for(TrendOccupancyNewModel trendOccupancyNewModel : trendOccupancyNewModels){
            try {
                TrendOccupancyNew trendOccupancyNew = new TrendOccupancyNew();

                trendOccupancyNew.setUser_id(buyer_id);

                trendOccupancyNew.setOccupied(Integer.parseInt(trendOccupancyNewModel.getOccupied()));
                trendOccupancyNew.setOccupancy(Double.parseDouble(trendOccupancyNewModel.getOccupancy()));
                trendOccupancyNew.setSpace(Integer.parseInt(trendOccupancyNewModel.getSpace()));
                trendOccupancyNew.setTotal_space(Integer.parseInt(trendOccupancyNewModel.getTotal_space()));
                trendOccupancyNew.setDate(Constant.SIMPLE_DATE_FORMAT_5.parse(trendOccupancyNewModel.getDate()));

                Optional<Warehouse> warehouse = warehouseDao.findByWarehouseName(trendOccupancyNewModel.getWarehouse().trim());

                if(!warehouse.isPresent()) {
                    skipped.add(trendOccupancyNewModel);

                    continue;
                }

                trendOccupancyNew.setWarehouse(warehouse.get().getId());

                Optional<TrendOccupancyNew> trendOccupancyNew2 = trendOccupancyNewDao.validateDuplicateTrendOccupancy(trendOccupancyNew.getUser_id(),
                        trendOccupancyNew.getWarehouse(),
                        Constant.SIMPLE_DATE_FORMAT_5.format(trendOccupancyNew.getDate()));

                if(trendOccupancyNew2.isPresent())
                    trendOccupancyNewDao.delete(trendOccupancyNew2.get());

                trendOccupancyNew.setCreated_at(new Date());
                trendOccupancyNew.setCreated_by(jwt.getUser_id());

                TrendOccupancyNew trendOccupancy = trendOccupancyNewDao.save(trendOccupancyNew);

                if(trendOccupancy.getId() > 0)
                    success++;
            }catch (Exception e){
                skipped.add(trendOccupancyNewModel);
            }

        }

        generalResponse.setData(UploadCsvModel.builder().skipped(skipped).success(success).build());
        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
        generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        return generalResponse;
    }

    @Override
    public GeneralResponse getWarehouseRentedByUser(int report, JWT jwt) {

        GeneralResponse generalResponse = new GeneralResponse();

        if(jwt.getRole_id()!=Constant.Role.BUYER.getRole()) {
            generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        List<WarehouseName> warehouseNameList = new ArrayList<>();

        if(report == 1) {
            warehouseNameList = trendOccupancyNewDao.getWarehouseRented(jwt.getUser_id());

        } else if(report == 2) {
            warehouseNameList = summaryWhStatusNewDao.getWarehouseRented(jwt.getUser_id());

        } else if(report == 3) {
            warehouseNameList = stockOnHandDao.getWarehouseRented(jwt.getUser_id());

        } else if(report == 4 || report == 5) {
            warehouseNameList = transactionListNewDao.getWarehouseRented(jwt.getUser_id());

        } else {
            generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            return generalResponse;
        }

        generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
        generalResponse.setData(warehouseNameList);

        return generalResponse;
    }
}
