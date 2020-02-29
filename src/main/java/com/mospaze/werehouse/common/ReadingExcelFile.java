package com.mospaze.werehouse.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mospaze.werehouse.entity.StockOnHand;
import com.mospaze.werehouse.entity.SummaryWhStatus;
import com.mospaze.werehouse.entity.TransactionList;
import com.mospaze.werehouse.entity.TrendOccupancy;
import com.mospaze.werehouse.model.*;
import com.mospaze.werehouse.repository.StockOnHandDao;
import com.mospaze.werehouse.repository.SummaryWhStatusDao;
import com.mospaze.werehouse.repository.TransactinListDao;
import com.mospaze.werehouse.repository.TrendOccupancyDao;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@Component
public class ReadingExcelFile {

    @Autowired
    private TrendOccupancyDao trendOccupancyDao;

    @Autowired
    private SummaryWhStatusDao summaryWhStatusDao;

    @Autowired
    private StockOnHandDao stockOnHandDao;

    @Autowired
    private TransactinListDao transactinListDao;

    public static final String EXCEL_FILE_PATH = "/Users/imankhaeruddin/Desktop/data for dashboard wms/trend_occupancy_1.xlsx";
    public static final String STOCK_ON_HAND_FILE_PATH = "/Users/imankhaeruddin/Desktop/data for dashboard wms/Copy of Trend occupancy.xlsx";
    public static final String TRANSACTION_LIST = "/Users/imankhaeruddin/Desktop/data for dashboard wms/WMS Report.xlsx";

    private static Occupancied occupancied = new Occupancied();
    private static Occupancy occupancy = new Occupancy();
    private static Space space = new Space();

    private static List<Occupancied> occupancieds = new ArrayList<>();
    private static List<Occupancy> occupancies = new ArrayList<>();
    private static List<Space> spaces = new ArrayList<>();

    // Create a DataFormatter to format and get each cell's value as String
    private static DataFormatter dataFormatter = new DataFormatter();

//    @PostConstruct
    @Transactional
    public void readFile() throws IOException, InvalidFormatException {

        trendOccupancyDao.deleteAll();

        Workbook workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> iterator = sheet.iterator();

        TrendOccupancyDataResponse trendOccupancyDataResponse = new TrendOccupancyDataResponse();
        List <TrendOccupancyData> trendOccupancyData = new ArrayList<>();
        TrendOccupancy trendOccupancy = new TrendOccupancy();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            List<Occupancied> occupancieds = new ArrayList<>();
            List<Occupancy> occupancies = new ArrayList<>();
            List<Space> spaces = new ArrayList<>();

            TrendOccupancyData trendOccupancyData1 = new TrendOccupancyData();
            Occupancied occupancied = new Occupancied();
            Occupancy occupancy = new Occupancy();
            Space space = new Space();


            if(nextRow.getRowNum() == 0 || nextRow.getRowNum() == 1 || nextRow.getRowNum() == 2 || nextRow.getRowNum() == 3)
                continue;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                occupancied = new Occupancied();
                occupancy = new Occupancy();
                space = new Space();

                int rows = cell.getRowIndex();
                int cells = cell.getColumnIndex();

                //fill date
                if(cells == 1) {
                    trendOccupancyData1.setDate(dataFormatter.formatCellValue(cell));
                }

                //fill occupied C3B
                if(cells == 27) {
                    occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
                    occupancied.setOccupied_name("C3B");
                    occupancieds.add(occupancied);
                }

                //fill occupied B2P
                if(cells == 28){
                    occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
                    occupancied.setOccupied_name("B2P");
                    occupancieds.add(occupancied);
                }

                //fill occupied B1L
                if(cells == 29){
                    occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
                    occupancied.setOccupied_name("B1L");
                    occupancieds.add(occupancied);
                }

                //fill total_occupied
                if(cells == 30) {
//                    trendOccupancyData1.setTotal_occupancied(dataFormatter.formatCellValue(cell));
                }

                //fill occupancy C3B
                if(cells == 31) {
                    occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
                    occupancy.setOccupancy_name("C3B");
                    occupancies.add(occupancy);
                }

                //fill occupancy B2P
                if(cells == 32){
                    occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
                    occupancy.setOccupancy_name("B2P");
                    occupancies.add(occupancy);
                }

                //fill occupancy B1L
                if(cells == 33){
                    occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
                    occupancy.setOccupancy_name("B1L");
                    occupancies.add(occupancy);
                }

                //fill total_occupancy
                if(cells == 34) {
//                    trendOccupancyData1.setTotal_occupancy(dataFormatter.formatCellValue(cell));
                }

                //fill space C3B
                if(cells == 35) {
                    space.setSpace_value(dataFormatter.formatCellValue(cell));
                    space.setSpace_name("C3B");
                    spaces.add(space);
                }

                //fill space B2P
                if(cells == 36){
                    space.setSpace_value(dataFormatter.formatCellValue(cell));
                    space.setSpace_name("B2P");
                    spaces.add(space);
                }

                //fill space B1L
                if(cells == 37){
                    space.setSpace_value(dataFormatter.formatCellValue(cell));
                    space.setSpace_name("B1L");
                    spaces.add(space);
                }

            }

            /*trendOccupancyData1.setOccupancy(occupancies);
            trendOccupancyData1.setOccupied(occupancieds);
            trendOccupancyData1.setSpace(spaces);*/

            trendOccupancyData.add(trendOccupancyData1);
        }

        trendOccupancyDataResponse.setWarehouse(Arrays.asList(new Warehouses("C3B", "1125"),
                new Warehouses("B2P", "900"),
                new Warehouses("B1L", "825")));
        trendOccupancyDataResponse.setTrend_occupancy_data(trendOccupancyData);

        ObjectMapper Obj = new ObjectMapper();

        System.out.println(Obj.writeValueAsString(trendOccupancyDataResponse));

        for(TrendOccupancyData occupancyData : trendOccupancyDataResponse.getTrend_occupancy_data()){
            trendOccupancy = new TrendOccupancy();

            /*if(occupancyData.getSpace().size() > 0){
                trendOccupancy.setDate(formatDate(occupancyData.getDate()));
                trendOccupancy.setMonth(getMonth(trendOccupancy.getDate().split("-")[1]));
                trendOccupancy.setTotal_occupancy(occupancyData.getTotal_occupancy());
                trendOccupancy.setTotal_occupied(occupancyData.getTotal_occupancied());
                System.err.println(trendOccupancy.getTotal_occupied());

                for(Occupancied occupancied : occupancyData.getOccupied()){
                    if(occupancied.getOccupied_name().equalsIgnoreCase("C3B"))
                        trendOccupancy.setOccupied_c3b(occupancied.getOccupied_value());
                    if(occupancied.getOccupied_name().equalsIgnoreCase("B2P"))
                        trendOccupancy.setOccupied_b2p(occupancied.getOccupied_value());
                    if(occupancied.getOccupied_name().equalsIgnoreCase("B1L"))
                        trendOccupancy.setOccupied_b1l(occupancied.getOccupied_value());
                }

                for(Occupancy occupancy : occupancyData.getOccupancy()){
                    if(occupancy.getOccupancy_name().equalsIgnoreCase("C3B"))
                        trendOccupancy.setOccupancy_c3b(occupancy.getOccupancy_value());
                    if(occupancy.getOccupancy_name().equalsIgnoreCase("B2P"))
                        trendOccupancy.setOccupancy_b2p(occupancy.getOccupancy_value());
                    if(occupancy.getOccupancy_name().equalsIgnoreCase("B1L"))
                        trendOccupancy.setOccupancy_b1l(occupancy.getOccupancy_value());
                }

                for(Space space : occupancyData.getSpace()){
                    if(space.getSpace_name().equalsIgnoreCase("C3B"))
                        trendOccupancy.setSpace_c3b(space.getSpace_value());
                    if(space.getSpace_name().equalsIgnoreCase("B2P"))
                        trendOccupancy.setSpace_b2p(space.getSpace_value());
                    if(space.getSpace_name().equalsIgnoreCase("B1L"))
                        trendOccupancy.setSpace_b1l(space.getSpace_value());
                }

                System.out.println("data : " + trendOccupancy.toString());
                trendOccupancyDao.save(trendOccupancy);
            }*/
        }

    }

//    @PostConstruct
    @Transactional
    public void readFileSummaryWhStatus() throws IOException, InvalidFormatException {

        summaryWhStatusDao.deleteAll();

        Workbook workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> iterator = sheet.iterator();

        SummaryWhStatus summaryWhStatus = new SummaryWhStatus();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            summaryWhStatus = new SummaryWhStatus();

            if(nextRow.getRowNum() == 0 || nextRow.getRowNum() == 1 || nextRow.getRowNum() == 2 || nextRow.getRowNum() == 3)
                continue;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                int rows = cell.getRowIndex();
                int cells = cell.getColumnIndex();

                //fill date
                if(cells == 1) {
                    summaryWhStatus.setDate(formatDate(dataFormatter.formatCellValue(cell)));
                    summaryWhStatus.setMonth(getMonth(summaryWhStatus.getDate().split("-")[1]));
                }

                /*=========================TOTAL_INBOUND===========================*/

                //fill inbound_c3b
                if(cells == 2) {
                    summaryWhStatus.setInbound_c3b(dataFormatter.formatCellValue(cell));
                }

                //fill inbound_b2p
                if(cells == 3) {
                    summaryWhStatus.setInbound_b2p(dataFormatter.formatCellValue(cell));
                }

                //fill inbound_b1l
                if(cells == 4) {
                    summaryWhStatus.setInbound_b1l(dataFormatter.formatCellValue(cell));
                }

                //fill total_inbound
                if(cells == 5) {
                    summaryWhStatus.setTotal_inbound(dataFormatter.formatCellValue(cell));
                }

                //fill total_inbound_per
                if(cells == 6) {
                    summaryWhStatus.setTotal_inb_per(dataFormatter.formatCellValue(cell));
                }

                /*=========================TOTAL_OUTBOUND===========================*/

                //fill outbound_c3b
                if(cells == 7) {
                    summaryWhStatus.setOutbound_c3b(dataFormatter.formatCellValue(cell));
                }

                //fill outbound_b2p
                if(cells == 8) {
                    summaryWhStatus.setOutbound_b2p(dataFormatter.formatCellValue(cell));
                }

                //fill outbound_b1l
                if(cells == 9) {
                    summaryWhStatus.setOutbound_b1l(dataFormatter.formatCellValue(cell));
                }

                //fill total_outbound
                if(cells == 10) {
                    summaryWhStatus.setTotal_outbound(dataFormatter.formatCellValue(cell));
                }

                //fill total_outbound_per
                if(cells == 11) {
                    summaryWhStatus.setTotal_out_per(dataFormatter.formatCellValue(cell));
                }

                /*=========================TOTAL_INBOUND===========================*/

                //fill total_inbound_c3b
                if(cells == 12) {
                    summaryWhStatus.setTotal_inbound_c3b(dataFormatter.formatCellValue(cell));
                }

                //fill total_inbound_b2p
                if(cells == 13) {
                    summaryWhStatus.setTotal_inbound_b2p(dataFormatter.formatCellValue(cell));
                }

                //fill total_inbound_b1l
                if(cells == 14) {
                    summaryWhStatus.setTotal_inbound_b1l(dataFormatter.formatCellValue(cell));
                }

                //fill total_inbound_tot
                if(cells == 15) {
                    summaryWhStatus.setTotal_inbound_tot(dataFormatter.formatCellValue(cell));
                }

                /*=========================TOTAL_OUTBOUND===========================*/

                //fill total_outbound_c3b
                if(cells == 16) {
                    summaryWhStatus.setTotal_outbound_c3b(dataFormatter.formatCellValue(cell));
                }

                //fill total_outbound_b2p
                if(cells == 17) {
                    summaryWhStatus.setTotal_outbound_b2p(dataFormatter.formatCellValue(cell));
                }

                //fill total_outbound_b1l
                if(cells == 18) {
                    summaryWhStatus.setTotal_outbound_b1l(dataFormatter.formatCellValue(cell));
                }

                //fill total_outbound_tot
                if(cells == 19) {
                    summaryWhStatus.setTotal_outbound_tot(dataFormatter.formatCellValue(cell));
                }

                /*=========================OCCUPIED===========================*/

                //fill occupied_c3b
                if(cells == 20) {
                    summaryWhStatus.setOccupied_c3b(dataFormatter.formatCellValue(cell));
                }

                //fill occupied_b2p
                if(cells == 21) {
                    summaryWhStatus.setOccupied_b2p(dataFormatter.formatCellValue(cell));
                }

                //fill occupied_b1l
                if(cells == 22) {
                    summaryWhStatus.setOccupied_b1l(dataFormatter.formatCellValue(cell));
                }

                //fill total_occupied
                if(cells == 23) {
                    summaryWhStatus.setTotal_occupied(dataFormatter.formatCellValue(cell));
                }

                //fill total_occupied_in_pp
                if(cells == 24) {
                    summaryWhStatus.setTotal_in_pp(dataFormatter.formatCellValue(cell));
                }

                /*=========================AVAILABLE_SPACE===========================*/

                //fill available_space
                if(cells == 25) {
                    summaryWhStatus.setAvailable_space(dataFormatter.formatCellValue(cell));
                }

            }

            summaryWhStatusDao.save(summaryWhStatus);
        }

    }

//    @PostConstruct
    @Transactional
    public void readFileStockOnHand() throws IOException, InvalidFormatException {

        stockOnHandDao.deleteAll();

        Workbook workbook = WorkbookFactory.create(new File(STOCK_ON_HAND_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(2);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> iterator = sheet.iterator();

        StockOnHand stockOnHand = new StockOnHand();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            stockOnHand = new StockOnHand();

            if(nextRow.getRowNum() == 0 || nextRow.getRowNum() == 325 || nextRow.getRowNum() == 326)
                continue;

            System.err.println(nextRow.getRowNum());

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                int rows = cell.getRowIndex();
                int cells = cell.getColumnIndex();

                //fill id
                if(cells == 0) {
                    stockOnHand.setId(Integer.parseInt(dataFormatter.formatCellValue(cell)));
                }

                //fill date
                if(cells == 8) {
                    String [] arr = dataFormatter.formatCellValue(cell).split("/");
                    String date = "20".concat(arr[2]).concat("-").concat(String.format("%02d" , Integer.parseInt(arr[0]))).concat("-").concat(String.format("%02d" , Integer.parseInt(arr[1])));
                    stockOnHand.setDate(date);
                    stockOnHand.setMonth(getMonth(stockOnHand.getDate().split("-")[1]));
                    stockOnHand.setReceive_date(dataFormatter.formatCellValue(cell));
                }

                //fill primary_key
                if(cells == 1) {
                    stockOnHand.setPrimary_key(dataFormatter.formatCellValue(cell));
                }

                //fill gedung
                if(cells == 2) {
                    stockOnHand.setGedung(dataFormatter.formatCellValue(cell));
                }

                //fill pallet_num
                if(cells == 3) {
                    stockOnHand.setPallet_num(dataFormatter.formatCellValue(cell));
                }

                //fill location
                if(cells == 4) {
                    stockOnHand.setLokasi(dataFormatter.formatCellValue(cell));
                }

                //fill sku
                if(cells == 5) {
                    stockOnHand.setSku(dataFormatter.formatCellValue(cell));
                }

                //fill desc
                if(cells == 6) {
                    stockOnHand.setDescription(dataFormatter.formatCellValue(cell));
                }

                //fill record_num
                if(cells == 7) {
                    stockOnHand.setRecord_num(dataFormatter.formatCellValue(cell));
                }

                //fill expire_date
                if(cells == 9) {
                    stockOnHand.setExpire_date(dataFormatter.formatCellValue(cell));
                }

                //fill batch
                if(cells == 10) {
                    stockOnHand.setBatch(dataFormatter.formatCellValue(cell));
                }

                //fill qty_release
                if(cells == 11) {
                    stockOnHand.setQty_release(Integer.parseInt(dataFormatter.formatCellValue(cell)));
                }

                //fill qty_outbound
                if(cells == 12) {
                    stockOnHand.setQty_outbound(Integer.parseInt(dataFormatter.formatCellValue(cell)));
                }

                //fill qty_stock
                if(cells == 13) {
                    stockOnHand.setQty_stock(Integer.parseInt(dataFormatter.formatCellValue(cell)));
                }

                //fill status_stock
                if(cells == 14) {
                    stockOnHand.setStatus_stock(dataFormatter.formatCellValue(cell));
                }

                //fill pallet
                if(cells == 15) {
                    stockOnHand.setPallet(dataFormatter.formatCellValue(cell));
                }

                //fill keterangan
                if(cells == 16) {
                    stockOnHand.setKeterangan(dataFormatter.formatCellValue(cell));
                }

            }

            stockOnHandDao.save(stockOnHand);
        }

    }

//    @PostConstruct
    @Transactional
    public void readFileTransactionList() throws IOException, InvalidFormatException, ParseException {

        transactinListDao.deleteAll();

        Workbook workbook = WorkbookFactory.create(new File(TRANSACTION_LIST));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(4);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        Iterator<Row> iterator = sheet.iterator();

        TransactionList transactionList = new TransactionList();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            transactionList = new TransactionList();

            if(nextRow.getRowNum() == 0)
                continue;

            System.err.println(nextRow.getRowNum());

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                int rows = cell.getRowIndex();
                int cells = cell.getColumnIndex();

                //fill sku
                if(cells == 1) {
                    transactionList.setSku(dataFormatter.formatCellValue(cell));
                }

                //fill job_no
                if(cells == 2) {
                    transactionList.setJob_no(dataFormatter.formatCellValue(cell));
                }

                //fill warehouse
                if(cells == 3) {
                    String warehouse = dataFormatter.formatCellValue(cell);

                    transactionList.setWarehouse(warehouse);

                    if(warehouse.equalsIgnoreCase("Warehouse Distribution Center 1"))
                        transactionList.setWarehouse("D2 Cikarang Delta Giri Wacana");

                    if(warehouse.equalsIgnoreCase("Warehouse Distribution Center 2"))
                        transactionList.setWarehouse("D2 Cikarang Kunci Logistics");
                }

                //fill location
                if(cells == 4) {
                    transactionList.setLocation(dataFormatter.formatCellValue(cell));
                }

                //fill batch
                if(cells == 5) {
                    transactionList.setBatch(dataFormatter.formatCellValue(cell));
                }

                //fill from_to
                if(cells == 6) {
                    transactionList.setFrom_to(dataFormatter.formatCellValue(cell));
                }

                //fill from_to
                if(cells == 5) {
                    transactionList.setSku(dataFormatter.formatCellValue(cell));
                }

                //fill qty
                if(cells == 7) {
                    transactionList.setQty(Integer.valueOf(dataFormatter.formatCellValue(cell)));
                }

                //fill packing_qty
                if(cells == 8) {
                    transactionList.setPacking_qty(Integer.valueOf(dataFormatter.formatCellValue(cell)));
                }

                //fill type
                if(cells == 9) {
                    transactionList.setType(dataFormatter.formatCellValue(cell));
                }

                //fill status
                if(cells == 11) {
                    transactionList.setStatus(dataFormatter.formatCellValue(cell));
                }

                //fill close_date
                if(cells == 13) {
                    String close_date = dataFormatter.formatCellValue(cell).replace("\\","");
                    System.err.println("close date : " + close_date);
                    if(close_date != null && !close_date.isEmpty())
                        transactionList.setClose_date(Constant.SIMPLE_DATE_FORMAT_5.parse(close_date));
                }

                //fill create_date
                if(cells == 15) {
                    String create_date = dataFormatter.formatCellValue(cell);
                    if (create_date != null && !create_date.isEmpty())
                        transactionList.setCreate_date(Constant.SIMPLE_DATE_FORMAT_5.parse(dataFormatter.formatCellValue(cell)));
                }

                //fill customer
                if(cells == 16) {
                    transactionList.setCustomer(dataFormatter.formatCellValue(cell));
                }

                //fill product_name
                if(cells == 17) {
                    transactionList.setProduct_name(dataFormatter.formatCellValue(cell));
                }

                //fill category
                if(cells == 18) {
                    transactionList.setCategory(dataFormatter.formatCellValue(cell));
                }

                //fill packing
                if(cells == 19) {
                    transactionList.setPacking(dataFormatter.formatCellValue(cell));
                }

                //fill transport_type
                if(cells == 20) {
                    transactionList.setTransport_type(dataFormatter.formatCellValue(cell));
                }

            }

            transactinListDao.save(transactionList);
        }

    }

    private String getMonth(String month) {
        if(month.equals("05"))
            return "May";
        else if(month.equals("06"))
            return "Juni";
        else if (month.equals("07"))
            return "Juli";
        else if (month.equals("08"))
            return "Aug";
        else
            return "Sep";
    }

    private String formatDate(String date) {
        String arr [] = date.split("-");
        if(arr[1].equals("May"))
            return "2019-05-" + arr[0];
        else if(arr[1].equals("Juni"))
            return "2019-06-" + arr[0];
        else if(arr[1].equals("Juli"))
            return "2019-07-" + arr[0];
        else if(arr[1].equals("Aug"))
            return "2019-08-" + arr[0];
        else
            return "2019-09-" + arr[0];
    }

    public static String readExcelFile(String month) throws IOException, InvalidFormatException {

        ObjectMapper obj = new ObjectMapper();

        Workbook workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH));

        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets");

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(1);

        Iterator<Row> iterator = sheet.iterator();

        TrendOccupancyDataResponse trendOccupancyDataResponse = new TrendOccupancyDataResponse();
        List <TrendOccupancyData> trendOccupancyDatas = new ArrayList<>();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            occupancieds = new ArrayList<>();
            occupancies = new ArrayList<>();
            spaces = new ArrayList<>();

            TrendOccupancyData trendOccupancyData = new TrendOccupancyData();

            if(nextRow.getRowNum() == 0 || nextRow.getRowNum() == 1 || nextRow.getRowNum() == 2 || nextRow.getRowNum() == 3)
                continue;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                occupancied = new Occupancied();
                occupancy = new Occupancy();
                space = new Space();

                if(month != null){
                    if(month.equalsIgnoreCase("Mei")){
                        if(dataFormatter.formatCellValue(cell).contains("May")){
                            occupancieds = getOccupancieds(cell);
                            occupancies = getOccupancies(cell);
                            spaces = getSpaces(cell);
                        } else {
                            continue;
                        }

                    } else if(month.equalsIgnoreCase("Juni")){
                        if(dataFormatter.formatCellValue(cell).contains("Juni")){
                            occupancieds = getOccupancieds(cell);
                            occupancies = getOccupancies(cell);
                            spaces = getSpaces(cell);
                        } else {
                            continue;
                        }
                    } else if(month.equalsIgnoreCase("Juli")){
                        if(dataFormatter.formatCellValue(cell).contains("Juli")){
                            occupancieds = getOccupancieds(cell);
                            occupancies = getOccupancies(cell);
                            spaces = getSpaces(cell);
                        } else {
                            continue;
                        }
                    } else if (month.equalsIgnoreCase("Agustus")){
                        if(dataFormatter.formatCellValue(cell).contains("Aug")){
                            occupancieds = getOccupancieds(cell);
                            occupancies = getOccupancies(cell);
                            spaces = getSpaces(cell);
                        } else {
                            continue;
                        }
                    } else {
                        return obj.writeValueAsString(new GeneralResponse(Constant.Code.ACTION_INVALID.getStatusCode(),
                                Constant.getInMessage(Constant.Code.ACTION_INVALID.getStatusCode()),
                                Constant.getEnMessage(Constant.Code.ACTION_INVALID.getStatusCode()),
                                null));
                    }
                } else {
                    occupancieds = getOccupancieds(cell);
                    occupancies = getOccupancies(cell);
                    spaces = getSpaces(cell);
                }

                //fill date
                if(cell.getColumnIndex() == 1) {
                    trendOccupancyData.setDate(dataFormatter.formatCellValue(cell));
                }

                //fill total_occupied
                /*if(cell.getColumnIndex() == 30)
                    trendOccupancyData.setTotal_occupancied(dataFormatter.formatCellValue(cell));

                //fill total_occupied
                if(cell.getColumnIndex() == 34)
                    trendOccupancyData.setTotal_occupancy(dataFormatter.formatCellValue(cell));*/
            }


            /*trendOccupancyData.setOccupancy(occupancies);
            trendOccupancyData.setOccupied(occupancieds);
            trendOccupancyData.setSpace(spaces);*/

            trendOccupancyDatas.add(trendOccupancyData);
        }

        trendOccupancyDataResponse.setWarehouse(Arrays.asList(new Warehouses("C3B", "1125"),
                new Warehouses("B2P", "900"),
                new Warehouses("B1L", "825")));
        trendOccupancyDataResponse.setTrend_occupancy_data(trendOccupancyDatas);

        return obj.writeValueAsString(trendOccupancyDataResponse);

    }

    private static List<Occupancied> getOccupancieds(Cell cell){
        //fill occupied C3B
        if(cell.getColumnIndex() == 27) {
            occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
            occupancied.setOccupied_name("C3B");
            occupancieds.add(occupancied);
        }

        //fill occupied B2P
        if(cell.getColumnIndex() == 28){
            occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
            occupancied.setOccupied_name("B2P");
            occupancieds.add(occupancied);
        }

        //fill occupied B1L
        if(cell.getColumnIndex() == 29){
            occupancied.setOccupied_value(dataFormatter.formatCellValue(cell));
            occupancied.setOccupied_name("B1L");
            occupancieds.add(occupancied);
        }

        return occupancieds;
    }

    private static List<Occupancy> getOccupancies(Cell cell){
        //fill occupancy C3B
        if(cell.getColumnIndex() == 31) {
            occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
            occupancy.setOccupancy_name("C3B");
            occupancies.add(occupancy);
        }

        //fill occupancy B2P
        if(cell.getColumnIndex() == 32){
            occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
            occupancy.setOccupancy_name("B2P");
            occupancies.add(occupancy);
        }

        //fill occupancy B1L
        if(cell.getColumnIndex() == 33){
            occupancy.setOccupancy_value(dataFormatter.formatCellValue(cell));
            occupancy.setOccupancy_name("B1L");
            occupancies.add(occupancy);
        }

        return occupancies;
    }

    private static List<Space> getSpaces(Cell cell){
        //fill space C3B
        if(cell.getColumnIndex() == 35) {
            space.setSpace_value(dataFormatter.formatCellValue(cell));
            space.setSpace_name("C3B");
            spaces.add(space);
        }

        //fill space B2P
        if(cell.getColumnIndex() == 36){
            space.setSpace_value(dataFormatter.formatCellValue(cell));
            space.setSpace_name("B2P");
            spaces.add(space);
        }

        //fill space B1L
        if(cell.getColumnIndex() == 37){
            space.setSpace_value(dataFormatter.formatCellValue(cell));
            space.setSpace_name("B1L");
            spaces.add(space);
        }

        return spaces;
    }
}
