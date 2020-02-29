package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.entity.Warehouse;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.WarehouseManagementRequest;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.SessionDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.repository.WarehouseDao;
import com.mospaze.werehouse.service.WarehouseService;
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
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_WAREHOUSE)
public class WarehouseManagementController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private Util util;

    @Autowired
    SessionDao sessionDao;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private WarehouseDao warehouseDao;

    /**
     * Return all warehouse with the given status with pageable. Buyer can not access to this
     * controller.
     *
     * @param username
     * @param session
     * @param status
     * @param name_city
     * @param page
     * @param size
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getWarehouseByStatus(@RequestHeader(name = "username", required = false) String username,
                                                                @RequestHeader(name = "session") String session,
                                                                @RequestParam(name = "status", required = false) Integer status,
                                                                @RequestParam(name = "search_value", required = false) String name_city,
                                                                @RequestParam(name = "page") Integer page,
                                                                @RequestParam(name = "size") Integer size) {

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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            if(role_login_user.get().getRole_id() == Constant.Role.BUYER.getRole()){

                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setData(warehouseService.getListWarehouseByStatus(status, name_city, role_login_user.get(), page, size));
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());

            
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

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for update warehouse data. Buyer can not access to this controller.
     *
     * @param username
     * @param session
     * @param warehouseManagementRequest
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.UPDATE)
    ResponseEntity<GeneralResponse> updateWarehouse(@RequestHeader(name = "username", required = false) String username,
                                                    @RequestHeader(name = "session") String session,
                                                    @Validated @RequestBody WarehouseManagementRequest warehouseManagementRequest,
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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            if(role_login_user.get().getRole_id() == Constant.Role.BUYER.getRole()){

                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(warehouseService.updateWarehouse(warehouseManagementRequest, username));
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
     * Delete warehouse with the given ID. Buyer can not access to this controller.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @DeleteMapping(value = RestConstant.DELETE)
    ResponseEntity<GeneralResponse> deleteWarehouse(@RequestHeader(name = "username", required = false) String username,
                                                    @RequestHeader(name = "session") String session,
                                                    @RequestParam(name = "id") int id) {

        logger.info("RequestParam [id] : {}", id);
        logger.info("USERNAME : " + username);
        logger.info("SESSION : " + session);

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            username = jwt.getUsername();

            generalResponse.setCode(warehouseService.deleteWarehouse(id, username));
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

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for change status verification of warehouse. Only Super Admin
     * and Admin can access this controller. There is 5 status verification of warehouse :
     *  - PENDING_VERIFICATION(1)
     *  - INFORMATION_VERIFICATION(2)
     *  - WAREHOUSE_INSPECTION(3)
     *  - APPROVED(4)
     *  - REJECTED(5)
     *
     * @param session
     * @param status_verification
     * @param display_status
     * @param id
     * @param is_send_notification
     * @param notes
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.CHANGE_STATUS)
    ResponseEntity<GeneralResponse> updateStatusVerificationWarehouse(@RequestHeader(name = "session") String session,
                                                                      @RequestParam(name = "status_verification") Integer status_verification,
                                                                      @RequestParam(name = "display_status") int display_status,
                                                                      @RequestParam(name = "id") int id,
                                                                      @RequestParam(name = "is_send_notification") int is_send_notification,
                                                                      @RequestParam(name = "notes", required = false) String notes) {

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

            String username = jwt.getUsername();

            generalResponse.setCode(warehouseService.changeStatusVerificationWarehouse(status_verification, display_status, id, username, is_send_notification, notes));
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
     * Retrieve all data warehouse by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getDetailWarehouse(@RequestHeader(name = "username", required = false) String username,
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

            generalResponse = warehouseService.getDetailWarehouseById(id, username);

            
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

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * Save warehouse data. Buyer can not access to this controller.
     *
     * @param username
     * @param session
     * @param warehouseManagementRequest
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.SAVE)
    ResponseEntity<GeneralResponse> insertWarehouse(@RequestHeader(name = "username", required = false) String username,
                                                    @RequestHeader(name = "session") String session,
                                                    @Valid @RequestBody WarehouseManagementRequest warehouseManagementRequest,
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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            if(role_login_user.get().getRole_id() == Constant.Role.BUYER.getRole() ||
                    (role_login_user.get().getRole_id() == Constant.Role.SELLER.getRole() && role_login_user.get().getModel_id() != warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_owner().getId())){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            if(warehouseDao.countByName(warehouseManagementRequest.getWarehouse_location_and_name().getWarehouse_name()) > 0){
                generalResponse.setCode(Constant.Code.WAREHOUSE_NAME_DUPLICATE.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            Warehouse warehouse = warehouseService.insertWarehouse(warehouseManagementRequest, role_login_user);

            generalResponse.setData(warehouse.getId());
            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
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

        return new ResponseEntity<>(generalResponse, generalResponse.getCode().equals(Constant.Code.SUCCESS.getStatusCode()) ? HttpStatus.CREATED : HttpStatus.OK);
    }

    /**
     * This controller used for get count notification warehouse status verification (PENDING_VERIFICATION(1) only).
     * Only Super Admin and Admin can access to this controller.
     *
     * @param username
     * @param session
     * @param httpServletRequest
     * @return {@link GeneralResponse}
     */
    @Deprecated
    @GetMapping(value = RestConstant.GET_NOTIFICATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getNotificationWarehouse(@RequestHeader(name = "username", required = false) String username,
                                                              @RequestHeader(name = "session") String session,
                                                              HttpServletRequest httpServletRequest) {

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            JWT jwt = util.parseJWTToken(session);

            if (jwt == null) {
                generalResponse.setCode(Constant.Code.SESSION_TIMEOUT.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            logger.info("JWT PARSED : {}",jwt.toString());

            /*username = jwt.getUsername();

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            if(role_login_user.get().getRole_id() != Constant.Role.ADMIN.getRole() &&
                    role_login_user.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole()){

                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }*/

            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            /*generalResponse.setData(warehouseService.getNotificationWarehouse(role_login_user.get()));*/

            
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

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for change partner warehouse. This changes will affected in search
     * warehouse order.
     *
     * @param session
     * @param status
     * @param id
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.UPDATE_PARTNER_WAREHOUSE)
    ResponseEntity<GeneralResponse> updateWarehousePartner(@RequestHeader(name = "session") String session,
                                                           @RequestParam(name = "status") int status,
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

            generalResponse.setCode(warehouseService.changeWarehousePartner(status, id, jwt));
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
     * This controller used for change first class warehouse. This changes will affected in Home mospaze
     * website.
     *
     * @param session
     * @param status
     * @param id
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.UPDATE_FIRST_CLASS_WAREHOUSE)
    ResponseEntity<GeneralResponse> updateWarehouseFirstClass(@RequestHeader(name = "session") String session,
                                                           @RequestParam(name = "status") int status,
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

            generalResponse.setCode(warehouseService.changeWarehouseFirstClass(status, id, jwt));
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
