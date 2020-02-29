package com.mospaze.werehouse.service;

import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
public interface TrendOccupancyService {
    /**
     * @param month
     * @param jwt
     * @param date
     * @param warehouse
     * @return {@link GeneralResponse}
     */
    GeneralResponse getTrendOccupancy(String month, JWT jwt, String date, int warehouse) throws ParseException;

    /**
     * @param month
     * @param jwt
     * @param date
     * @param warehouse
     * @return {@link GeneralResponse}
     */
    GeneralResponse getSummaryWhStatus(String month, JWT jwt, String date, int warehouse) throws ParseException;

    /**
     * @param month
     * @param jwt
     * @param date
     * @param warehouse
     * @param start_date
     * @param end_date
     * @return {@link GeneralResponse}
     */
    GeneralResponse getOccupancyProductFamily(String month, JWT jwt, String date, int warehouse, String start_date, String end_date) throws ParseException;

    /**
     * @param warehouse
     * @param jwt
     * @param page
     * @param size
     * @param status
     * @return {@link GeneralResponse}
     */
    GeneralResponse getJobList(int warehouse, JWT jwt, int page, int size, String status);

    /**
     * @param warehouse
     * @param jwt
     * @return {@link GeneralResponse}
     */
    GeneralResponse getStockStatus(int warehouse, JWT jwt);

    /**
     * @param file
     * @param buyer_id
     * @param jwt
     * @return {@link GeneralResponse}
     */
    GeneralResponse uploadTrendOccupancy(MultipartFile file, int buyer_id, JWT jwt) throws IOException, ParseException;

    /**
     * @param report
     * @param jwt
     * @return {@link GeneralResponse}
     */
    GeneralResponse getWarehouseRentedByUser(int report, JWT jwt);
}
