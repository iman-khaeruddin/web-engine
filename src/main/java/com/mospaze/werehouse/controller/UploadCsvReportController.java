package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.service.StockOnHandService;
import com.mospaze.werehouse.service.SummaryWhStatusService;
import com.mospaze.werehouse.service.TransactionListService;
import com.mospaze.werehouse.service.TrendOccupancyService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Iman Khaeruddin
 * @since 06/12/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(RestConstant.INDEX_UPLOAD)
public class UploadCsvReportController {

    private Logger logger = LoggerFactory.getLogger(UploadCsvReportController.class);

    @Autowired
    private Util util;

    @Autowired
    private TrendOccupancyService trendOccupancyService;

    @Autowired
    private SummaryWhStatusService summaryWhStatusService;

    @Autowired
    private StockOnHandService stockOnHandService;

    @Autowired
    private TransactionListService transactionListService;

    @Transactional
    @PostMapping(value = RestConstant.TREND_OCCUPANCY_)
    public ResponseEntity<GeneralResponse> uploadCsvTrendOccupancy(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("buyer_id") int buyer_id,
                                                           @RequestHeader(name = "session") String session) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {

            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            generalResponse = trendOccupancyService.uploadTrendOccupancy(file, buyer_id, jwt);

        } catch (MalformedJwtException mje){
            logger.error("see stack trace, {}", mje.getMessage());
            logger.error(mje.getMessage());
            mje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (ExpiredJwtException eje){
            logger.error("see stack trace, {}", eje.getMessage());
            logger.error(eje.getMessage());
            eje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (SignatureException se){
            logger.error("see stack trace, {}", se.getMessage());
            logger.error(se.getMessage());
            se.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (IncorrectClaimException ice){
            logger.error("see stack trace, {}", ice.getMessage());
            logger.error(ice.getMessage());
            ice.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (NullPointerException npe){
            logger.error("see stack trace, {}", npe.getMessage());
            logger.error(npe.getMessage());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(e.getMessage());
            generalResponse.setId_message(e.getMessage());
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = RestConstant.SUMMARY_WH_STATUS_)
    public ResponseEntity<GeneralResponse> uploadCsvSummaryWhStatus(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("buyer_id") int buyer_id,
                                                           @RequestHeader(name = "session") String session) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {

            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            generalResponse = summaryWhStatusService.uploadSummaryWhStatus(file, buyer_id, jwt);

        } catch (MalformedJwtException mje){
            logger.error("see stack trace, {}", mje.getMessage());
            logger.error(mje.getMessage());
            mje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (ExpiredJwtException eje){
            logger.error("see stack trace, {}", eje.getMessage());
            logger.error(eje.getMessage());
            eje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (SignatureException se){
            logger.error("see stack trace, {}", se.getMessage());
            logger.error(se.getMessage());
            se.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (IncorrectClaimException ice){
            logger.error("see stack trace, {}", ice.getMessage());
            logger.error(ice.getMessage());
            ice.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (NullPointerException npe){
            logger.error("see stack trace, {}", npe.getMessage());
            logger.error(npe.getMessage());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(e.getMessage());
            generalResponse.setId_message(e.getMessage());
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = RestConstant.STOCK_ON_HAND)
    public ResponseEntity<GeneralResponse> uploadCsvStockOnHand(@RequestParam("file") MultipartFile file,
                                                                    @RequestParam("buyer_id") int buyer_id,
                                                                    @RequestHeader(name = "session") String session) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {

            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            generalResponse = stockOnHandService.uploadStockOnHand(file, buyer_id, jwt);

        } catch (MalformedJwtException mje){
            logger.error("see stack trace, {}", mje.getMessage());
            logger.error(mje.getMessage());
            mje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (ExpiredJwtException eje){
            logger.error("see stack trace, {}", eje.getMessage());
            logger.error(eje.getMessage());
            eje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (SignatureException se){
            logger.error("see stack trace, {}", se.getMessage());
            logger.error(se.getMessage());
            se.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (IncorrectClaimException ice){
            logger.error("see stack trace, {}", ice.getMessage());
            logger.error(ice.getMessage());
            ice.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (NullPointerException npe){
            logger.error("see stack trace, {}", npe.getMessage());
            logger.error(npe.getMessage());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(e.getMessage());
            generalResponse.setId_message(e.getMessage());
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = RestConstant.JOB_LIST_)
    public ResponseEntity<GeneralResponse> uploadCsvTransactionList(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("buyer_id") int buyer_id,
                                                                @RequestHeader(name = "session") String session) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {

            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            generalResponse = transactionListService.uploadTransactionList(file, buyer_id, jwt);

        } catch (MalformedJwtException mje){
            logger.error("see stack trace, {}", mje.getMessage());
            logger.error(mje.getMessage());
            mje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (ExpiredJwtException eje){
            logger.error("see stack trace, {}", eje.getMessage());
            logger.error(eje.getMessage());
            eje.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (SignatureException se){
            logger.error("see stack trace, {}", se.getMessage());
            logger.error(se.getMessage());
            se.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (IncorrectClaimException ice){
            logger.error("see stack trace, {}", ice.getMessage());
            logger.error(ice.getMessage());
            ice.printStackTrace();

            generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (NullPointerException npe){
            logger.error("see stack trace, {}", npe.getMessage());
            logger.error(npe.getMessage());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(e.getMessage());
            generalResponse.setId_message(e.getMessage());
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }
}
