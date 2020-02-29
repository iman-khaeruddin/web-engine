package com.mospaze.werehouse.service.impl;

import com.mospaze.werehouse.model.DataTrackerReportResponse;
import com.mospaze.werehouse.repository.CustomDataTrackerDao;
import com.mospaze.werehouse.service.DataTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 */
@Service
public class DataTrackerServiceImpl implements DataTrackerService {

    @Autowired
    private CustomDataTrackerDao customDataTrackerDao;

    @Override
    public List<DataTrackerReportResponse> reportDataTrackerOs(String month, String date) {
        List<Object[]> objects = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseTemp = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseList = new ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        objects = customDataTrackerDao.findDataTrackerByOs(month, date);

        //serialize to dataTrackerReportResponseTemp
        for(Object [] objects1 : objects){
            DataTrackerReportResponse dataTrackerReportResponse = new DataTrackerReportResponse();
            String jml = String.valueOf(objects1[0]);
            dataTrackerReportResponse.setQty(Integer.valueOf(jml));
            String os = (String) objects1[1];
            dataTrackerReportResponse.setOs(os != null ? os : "");

            dataTrackerReportResponseTemp.add(dataTrackerReportResponse);
        }

        //sum grand total
        Integer total_qty = dataTrackerReportResponseTemp.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(DataTrackerReportResponse dataTrackerReportResponse : dataTrackerReportResponseTemp){
            DataTrackerReportResponse dataTrackerReportResponse1 = new DataTrackerReportResponse();

            dataTrackerReportResponse1.setOs(dataTrackerReportResponse.getOs());
            dataTrackerReportResponse1.setQty(dataTrackerReportResponse.getQty());
            dataTrackerReportResponse1.setPercentage(String.valueOf(formatter.format((dataTrackerReportResponse.getQty() / total_qty.doubleValue()) * 100)));
            dataTrackerReportResponse1.setGrand_total(total_qty);

            dataTrackerReportResponseList.add(dataTrackerReportResponse1);
        }

        return dataTrackerReportResponseList;
    }

    @Override
    public List<DataTrackerReportResponse> reportDataTrackerBrowser(String month, String date) {
        List<Object[]> objects = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseTemp = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseList = new ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        objects = customDataTrackerDao.findDataTrackerByBrowser(month, date);

        //serialize to dataTrackerReportResponseTemp
        for(Object [] objects1 : objects){
            DataTrackerReportResponse dataTrackerReportResponse = new DataTrackerReportResponse();
            String jml = String.valueOf(objects1[0]);
            dataTrackerReportResponse.setQty(Integer.valueOf(jml));
            String browser = (String) objects1[1];
            dataTrackerReportResponse.setBrowser(browser != null ? browser : "");

            dataTrackerReportResponseTemp.add(dataTrackerReportResponse);
        }

        //sum grand total
        Integer total_qty = dataTrackerReportResponseTemp.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(DataTrackerReportResponse dataTrackerReportResponse : dataTrackerReportResponseTemp){
            DataTrackerReportResponse dataTrackerReportResponse1 = new DataTrackerReportResponse();

            dataTrackerReportResponse1.setBrowser(dataTrackerReportResponse.getBrowser());
            dataTrackerReportResponse1.setQty(dataTrackerReportResponse.getQty());
            dataTrackerReportResponse1.setPercentage(String.valueOf(formatter.format((dataTrackerReportResponse.getQty() / total_qty.doubleValue()) * 100)));
            dataTrackerReportResponse1.setGrand_total(total_qty);

            dataTrackerReportResponseList.add(dataTrackerReportResponse1);
        }

        return dataTrackerReportResponseList;
    }

    @Override
    public List<DataTrackerReportResponse> reportDataTrackerIpAddress(String month, String date) {
        List<Object[]> objects = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseTemp = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseList = new ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        objects = customDataTrackerDao.findDataTrackerByIpAddress(month, date);

        //serialize to dataTrackerReportResponseTemp
        for(Object [] objects1 : objects){
            DataTrackerReportResponse dataTrackerReportResponse = new DataTrackerReportResponse();
            String jml = String.valueOf(objects1[0]);
            dataTrackerReportResponse.setQty(Integer.valueOf(jml));
            String ip_address = (String) objects1[1];
            dataTrackerReportResponse.setIp_address(ip_address != null ? ip_address : "");

            dataTrackerReportResponseTemp.add(dataTrackerReportResponse);
        }

        //sum grand total
        Integer total_qty = dataTrackerReportResponseTemp.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(DataTrackerReportResponse dataTrackerReportResponse : dataTrackerReportResponseTemp){
            DataTrackerReportResponse dataTrackerReportResponse1 = new DataTrackerReportResponse();

            dataTrackerReportResponse1.setIp_address(dataTrackerReportResponse.getIp_address());
            dataTrackerReportResponse1.setQty(dataTrackerReportResponse.getQty());
            dataTrackerReportResponse1.setPercentage(String.valueOf(formatter.format((dataTrackerReportResponse.getQty() / total_qty.doubleValue()) * 100)));
            dataTrackerReportResponse1.setGrand_total(total_qty);

            dataTrackerReportResponseList.add(dataTrackerReportResponse1);
        }

        return dataTrackerReportResponseList;
    }

    @Override
    public List<DataTrackerReportResponse> reportDataTrackerCity(String month, String date) {
        List<Object[]> objects = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseTemp = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseList = new ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        objects = customDataTrackerDao.findDataTrackerByCity(month, date);

        //serialize to dataTrackerReportResponseTemp
        for(Object [] objects1 : objects){
            DataTrackerReportResponse dataTrackerReportResponse = new DataTrackerReportResponse();
            String jml = String.valueOf(objects1[0]);
            dataTrackerReportResponse.setQty(Integer.valueOf(jml));
            String city = (String) objects1[1];
            dataTrackerReportResponse.setCity(city != null ? city : "");

            dataTrackerReportResponseTemp.add(dataTrackerReportResponse);
        }

        //sum grand total
        Integer total_qty = dataTrackerReportResponseTemp.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(DataTrackerReportResponse dataTrackerReportResponse : dataTrackerReportResponseTemp){
            DataTrackerReportResponse dataTrackerReportResponse1 = new DataTrackerReportResponse();

            dataTrackerReportResponse1.setCity(dataTrackerReportResponse.getCity());
            dataTrackerReportResponse1.setQty(dataTrackerReportResponse.getQty());
            dataTrackerReportResponse1.setPercentage(String.valueOf(formatter.format((dataTrackerReportResponse.getQty() / total_qty.doubleValue()) * 100)));
            dataTrackerReportResponse1.setGrand_total(total_qty);

            dataTrackerReportResponseList.add(dataTrackerReportResponse1);
        }

        return dataTrackerReportResponseList;
    }

    @Override
    public List<DataTrackerReportResponse> reportDataTrackerDistrict(String month, String date) {
        List<Object[]> objects = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseTemp = new ArrayList<>();
        List<DataTrackerReportResponse> dataTrackerReportResponseList = new ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#0.00");

        objects = customDataTrackerDao.findDataTrackerByDistrict(month, date);

        //serialize to dataTrackerReportResponseTemp
        for(Object [] objects1 : objects){
            DataTrackerReportResponse dataTrackerReportResponse = new DataTrackerReportResponse();
            String jml = String.valueOf(objects1[0]);
            dataTrackerReportResponse.setQty(Integer.valueOf(jml));
            String district = (String) objects1[1];
            dataTrackerReportResponse.setDistrict(district != null ? district : "");

            dataTrackerReportResponseTemp.add(dataTrackerReportResponse);
        }

        //sum grand total
        Integer total_qty = dataTrackerReportResponseTemp.stream().map(x -> x.getQty()).reduce(0, Integer::sum);

        for(DataTrackerReportResponse dataTrackerReportResponse : dataTrackerReportResponseTemp){
            DataTrackerReportResponse dataTrackerReportResponse1 = new DataTrackerReportResponse();

            dataTrackerReportResponse1.setDistrict(dataTrackerReportResponse.getDistrict());
            dataTrackerReportResponse1.setQty(dataTrackerReportResponse.getQty());
            dataTrackerReportResponse1.setPercentage(String.valueOf(formatter.format((dataTrackerReportResponse.getQty() / total_qty.doubleValue()) * 100)));
            dataTrackerReportResponse1.setGrand_total(total_qty);

            dataTrackerReportResponseList.add(dataTrackerReportResponse1);
        }

        return dataTrackerReportResponseList;
    }
}
