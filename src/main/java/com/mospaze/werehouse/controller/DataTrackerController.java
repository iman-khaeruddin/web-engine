package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.model.DataTrackerReportResponse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.service.DataTrackerService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author Iman Khaeruddin
 * @since 23/12/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_DATA_TRACKER)
public class DataTrackerController {

    private final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    private DataTrackerService dataTrackerService;

    @Autowired
    private Util util;

    /**
     * This controller used for get data tracker group by Operating System.
     *
     * @param session
     * @param month
     * @param date
     * @param type
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_DATA_TRACKER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getDataTrackerByOs(@RequestHeader(name = "session") String session,
                                                              @RequestParam(name = "month", required = false) String month,
                                                              @RequestParam(name = "date", required = false) String date,
                                                              @PathVariable("type") String type) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            if(!Constant.DataTrackerTypeList().contains(type)){
                generalResponse.setCode(Constant.Code.WRONG_PATH_VARIABLE.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.BAD_REQUEST);
            }

            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            if(jwt.getRole_id() != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            if(month != null)
                Constant.SIMPLE_DATE_FORMAT_6.parse(month);

            if(date != null)
                Constant.SIMPLE_DATE_FORMAT_5.parse(date);

            if(type.equals(Constant.DataTrackerType.OS.getType()))
                generalResponse.setData(dataTrackerService.reportDataTrackerOs(month, date));

            if(type.equals(Constant.DataTrackerType.BROWSER.getType()))
                generalResponse.setData(dataTrackerService.reportDataTrackerBrowser(month, date));

            if(type.equals(Constant.DataTrackerType.IP_ADDRESS.getType()))
                generalResponse.setData(dataTrackerService.reportDataTrackerIpAddress(month, date));

            if(type.equals(Constant.DataTrackerType.CITY.getType()))
                generalResponse.setData(dataTrackerService.reportDataTrackerCity(month, date));

            if(type.equals(Constant.DataTrackerType.DISTRICT.getType()))
                generalResponse.setData(dataTrackerService.reportDataTrackerDistrict(month, date));

            List<DataTrackerReportResponse> list = (List<DataTrackerReportResponse>) generalResponse.getData();

            if(list.size() > 0) {
                generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            } else {
                generalResponse.setData(null);
                generalResponse.setCode(Constant.Code.DATA_NOT_FOUND.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            }

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
        } catch (NullPointerException npe) {
            logger.error("see stack trace : {}", npe.getMessage());
            logger.error(npe.toString());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (ParseException npe) {
            logger.error("see stack trace : {}", npe.getMessage());
            logger.error(npe.toString());
            npe.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(npe.getMessage());
            generalResponse.setId_message(npe.getMessage());
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
}
