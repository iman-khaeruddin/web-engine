package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.*;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.GeneralResponseNew;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(RestConstant.INDEX_MASTER_DATA)
public class MasterDataController {

    private Logger logger = LoggerFactory.getLogger(MasterDataController.class);

    @Autowired
    private MsWarehouseAutomationService msWarehouseAutomationService;

    @Autowired
    private MsWarehouseCategoryService msWarehouseCategoryService;

    @Autowired
    private MsWarehouseFeatureService msWarehouseFeatureService;

    @Autowired
    private MsWarehouseServiceService msWarehouseServiceService;

    @Autowired
    private com.mospaze.werehouse.service.MsWarehouseSpaceService msWarehouseSpaceService;

    @Autowired
    private MsWarehouseSpaceServiceService msWarehouseSpaceServiceService;

    @Autowired
    private MsWarehouseTypeService msWarehouseTypeService;

    @Autowired
    private MsUnitService msUnitService;

    @Autowired
    private MsCitiesService msCitiesService;

    @Autowired
    private MsWarehouseTemperatureService msWarehouseTemperatureService;

    @Autowired
    private MsWarehouseOperatingHourService msWarehouseOperatingHourService;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private Util util;

    /**
     * Return all master features with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_FEATURE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterFeature(@RequestHeader(name = "session") String session,
                                                               @RequestParam(name = "page") Integer page,
                                                               @RequestParam(name = "size") Integer size,
                                                               @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponseNew generalResponseNew = new GeneralResponseNew();
        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseFeatureService.getMasterFeature(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master feature by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_FEATURE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterFeatureById(@RequestHeader(name = "username", required = false) String username,
                                                   @RequestHeader(name = "session") String session,
                                                   @PathVariable("id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseFeatureService.getMasterFeatureById(id);

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

    /**
     * Update master feature with the given PARAMs.
     *
     * @param username
     * @param session
     * @param msWarehouseFeature
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_FEATURE_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterFeature(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseFeature msWarehouseFeature,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseFeatureService.updateMasterFeature(msWarehouseFeature));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [features_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.FEATURE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master feature.
     *
     * @param username
     * @param session
     * @param msWarehouseFeature
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_FEATURE_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterFeature(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseFeature msWarehouseFeature,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseFeatureService.insertMasterFeature(msWarehouseFeature));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [features_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.FEATURE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master feature by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_FEATURE_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterFeature(@RequestHeader(name = "username") String username,
                                               @RequestHeader(name = "session") String session,
                                               @RequestParam(name = "id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseFeatureService.deleteMasterFeature(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

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

    /**
     * Return all master service with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_SERVICE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterService(@RequestHeader(name = "session") String session,
                                                            @RequestParam(name = "page") Integer page,
                                                            @RequestParam(name = "size") Integer size,
                                                            @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponse generalResponse = new GeneralResponse();
        GeneralResponseNew generalResponseNew = new GeneralResponseNew();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseServiceService.getMasterService(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master service by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_SERVICE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterServiceById(@RequestHeader(name = "username", required = false) String username,
                                                                @RequestHeader(name = "session") String session,
                                                                @PathVariable("id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseServiceService.getMasterServiceById(id);

            
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

    /**
     * Update master service with the given PARAMs.
     *
     * @param username
     * @param session
     * @param msWarehouseService
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_SERVICE_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterService(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseService msWarehouseService,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseServiceService.updateMasterService(msWarehouseService));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [services_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.SERVICE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master service.
     *
     * @param username
     * @param session
     * @param msWarehouseService
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_SERVICE_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterService(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseService msWarehouseService,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseServiceService.insertMasterService(msWarehouseService));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [services_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.SERVICE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master service by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_SERVICE_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterService(@RequestHeader(name = "username", required = false) String username,
                                                        @RequestHeader(name = "session") String session,
                                                        @RequestParam(name = "id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseServiceService.deleteMasterService(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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

    /**
     * Return all master automation with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_AUTOMATION_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterAutomation(@RequestHeader(name = "session") String session,
                                                            @RequestParam(name = "page") Integer page,
                                                            @RequestParam(name = "size") Integer size,
                                                            @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponse generalResponse = new GeneralResponse();
        GeneralResponseNew generalResponseNew = new GeneralResponseNew();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseAutomationService.getMasterAutomation(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master automation by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_AUTOMATION_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterAutomationById(@RequestHeader(name = "username", required = false) String username,
                                                                @RequestHeader(name = "session") String session,
                                                                @PathVariable("id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseAutomationService.getMasterAutomationById(id);

            
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

    /**
     * Update master automation with the given PARAMs.
     *
     * @param username
     * @param session
     * @param msWarehouseAutomation
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_AUTOMATION_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterAutomation(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseAutomation msWarehouseAutomation,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseAutomationService.updateMasterAutomation(msWarehouseAutomation));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [automation_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.AUTOMATION_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master automation.
     *
     * @param username
     * @param session
     * @param msWarehouseAutomation
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_AUTOMATION_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterAutomation(@RequestHeader(name = "username", required = false) String username,
                                                               @RequestHeader(name = "session") String session,
                                                               @Validated @RequestBody MsWarehouseAutomation msWarehouseAutomation,
                                                               BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseAutomationService.insertMasterAutomation(msWarehouseAutomation));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [automation_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.AUTOMATION_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master automation by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_AUTOMATION_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterAutomation(@RequestHeader(name = "username", required = false) String username,
                                                        @RequestHeader(name = "session") String session,
                                                        @RequestParam(name = "id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseAutomationService.deleteMasterAutomation(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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

    /**
     * Return all master type with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_TYPE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterType(@RequestHeader(name = "session") String session,
                                                         @RequestParam(name = "page") Integer page,
                                                         @RequestParam(name = "size") Integer size,
                                                         @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponseNew generalResponseNew = new GeneralResponseNew();
        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseTypeService.getMasterType(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master type by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_TYPE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterTypeById(@RequestHeader(name = "username", required = false) String username,
                                                             @RequestHeader(name = "session") String session,
                                                             @PathVariable("id") int id) {

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

            username = jwt.getUsername();
            
            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseTypeService.getMasterTypeById(id);

            
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

    /**
     * Update master type with the given PARAMs.
     *
     * @param username
     * @param session
     * @param msWarehouseType
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_TYPE_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterType(@RequestHeader(name = "username", required = false) String username,
                                                            @RequestHeader(name = "session") String session,
                                                            @Validated @RequestBody MsWarehouseType msWarehouseType,
                                                            BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseTypeService.updateMasterType(msWarehouseType));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [type_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.TYPE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master type.
     *
     * @param username
     * @param session
     * @param msWarehouseType
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_TYPE_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterType(@RequestHeader(name = "username", required = false) String username,
                                                            @RequestHeader(name = "session") String session,
                                                            @Validated @RequestBody MsWarehouseType msWarehouseType,
                                                            BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseTypeService.insertMasterType(msWarehouseType));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [type_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.TYPE_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master type by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_TYPE_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterType(@RequestHeader(name = "username", required = false) String username,
                                                     @RequestHeader(name = "session") String session,
                                                     @RequestParam(name = "id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseTypeService.deleteMasterType(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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

    /**
     * Return all master category with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_CATEGORY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterCategory(@RequestHeader(name = "session") String session,
                                                         @RequestParam(name = "page") Integer page,
                                                         @RequestParam(name = "size") Integer size,
                                                         @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponseNew generalResponseNew = new GeneralResponseNew();
        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(jwt.getUsername()).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseCategoryService.getMasterCategory(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master category by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_CATEGORY_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterCategoryById(@RequestHeader(name = "username", required = false) String username,
                                                             @RequestHeader(name = "session") String session,
                                                             @PathVariable("id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseCategoryService.getMasterCategoryById(id);

            
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

    /**
     * Update master category with the given PARAMs.
     *
     * @param username
     * @param session
     * @param msWarehouseCategory
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_CATEGORY_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterCategory(@RequestHeader(name = "username", required = false) String username,
                                                            @RequestHeader(name = "session") String session,
                                                            @Validated @RequestBody MsWarehouseCategory msWarehouseCategory,
                                                            BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseCategoryService.updateMasterCategory(msWarehouseCategory));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [category_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.CATEGORY_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master category.
     *
     * @param username
     * @param session
     * @param msWarehouseCategory
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_CATEGORY_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterCategory(@RequestHeader(name = "username", required = false) String username,
                                                            @RequestHeader(name = "session") String session,
                                                            @Validated @RequestBody MsWarehouseCategory msWarehouseCategory,
                                                            BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseCategoryService.insertMasterCategory(msWarehouseCategory));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [category_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.CATEGORY_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master category by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_CATEGORY_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterCategory(@RequestHeader(name = "username", required = false) String username,
                                                        @RequestHeader(name = "session") String session,
                                                        @RequestParam(name = "id") int id) {

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

            username = jwt.getUsername();

            int role_id = modelHasRoleDao.findRoleByUserId(usersDao.getUserByPhone(username).get().getId());
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseCategoryService.deleteMasterCategory(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));

            
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

    /**
     * Return all master operating hour with the given PARAMs with pageable.
     *
     * @param session
     * @param page
     * @param size
     * @param search_value
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_OPERATING_HOUR_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseNew> getMasterOperatingHour(@RequestHeader(name = "session") String session,
                                                             @RequestParam(name = "page") Integer page,
                                                             @RequestParam(name = "size") Integer size,
                                                             @RequestParam(name = "search_value", required = false) String search_value) {

        GeneralResponseNew generalResponseNew = new GeneralResponseNew();
        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = jwt.getRole_id();
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
                generalResponseNew.setGeneral_response(generalResponse);

                return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
            }

            generalResponseNew = msWarehouseOperatingHourService.getMasterOperatingHour(page, size, search_value);
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        generalResponseNew.setGeneral_response(generalResponse);

        return new ResponseEntity<>(generalResponseNew, HttpStatus.OK);
    }

    /**
     * Retrieves master operating hour by its ID.
     *
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_MASTER_OPERATING_HOUR_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getMasterOperatingHourById(@RequestHeader(name = "session") String session,
                                                                 @PathVariable("id") int id) {

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

            int role_id = jwt.getRole_id();
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse = msWarehouseOperatingHourService.getMasterOperatingHourById(id);


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

    /**
     * Update master operating hour with the given PARAMs.
     *
     * @param session
     * @param msWarehouseOperatingHour
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.MASTER_OPERATING_HOUR_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> updateMasterOperatingHour(@RequestHeader(name = "session") String session,
                                                                @Validated @RequestBody MsWarehouseOperatingHour msWarehouseOperatingHour,
                                                                BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = jwt.getRole_id();
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseOperatingHourService.updateMasterOperatingHour(msWarehouseOperatingHour));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));


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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [category_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.CATEGORY_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save master operating hour.
     *
     * @param session
     * @param msWarehouseOperatingHour
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.MASTER_OPERATING_HOUR_INSERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> insertMasterOperatingHour(@RequestHeader(name = "session") String session,
                                                                @Validated @RequestBody MsWarehouseOperatingHour msWarehouseOperatingHour,
                                                                BindingResult bindingResult) {

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            int role_id = jwt.getRole_id();
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseOperatingHourService.insertMasterOperatingHour(msWarehouseOperatingHour));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));


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
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("constraint [category_name_UNIQUE]"))
                generalResponse.setCode(Constant.Code.CATEGORY_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Deleted master category by its ID.
     *
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.MASTER_OPERATING_HOUR_DELETE)
    ResponseEntity<GeneralResponse> deleteMasterOperatingHour(@RequestHeader(name = "session") String session,
                                                         @RequestParam(name = "id") int id) {

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

            int role_id = jwt.getRole_id();
            if(role_id != Constant.Role.ADMIN.getRole() && role_id != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(msWarehouseOperatingHourService.deleteMasterOperatingHour(id));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));


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
