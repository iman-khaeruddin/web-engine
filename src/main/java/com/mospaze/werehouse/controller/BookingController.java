package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.GeneratePDFFile;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.model.BookingRequestModel;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.OrderRequestModel;
import com.mospaze.werehouse.repository.BookingRequestDao;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.BookingService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_BOOKING)
public class BookingController {

    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private Util util;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private GeneratePDFFile generatePDFFile;

    @Autowired
    private BookingRequestDao bookingRequestDao;

    /**
     * This controller used for booking warehouse. Can be accessed by buyer only.
     *
     * @param username
     * @param session
     * @param bookingRequestModel
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.INDEX_WAREHOUSE)
    @ResponseBody
    ResponseEntity<GeneralResponse> bookingWarehouse(@RequestHeader(name = "username", required = false) String username,
                                                     @RequestHeader(name = "session") String session,
                                                     @Validated @RequestBody BookingRequestModel bookingRequestModel,
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

            generalResponse.setCode(bookingService.bookingProcess(bookingRequestModel, username));
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

        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }

    /**
     * Return all booking warehouse list with the given status with pageable.
     *
     * @param username
     * @param session
     * @param status
     * @param warehouse_name
     * @param page
     * @param size
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getBookingWarehouseByStatus(@RequestHeader(name = "username", required = false) String username,
                                                                       @RequestHeader(name = "session") String session,
                                                                       @RequestParam(name = "status", required = false) Integer status,
                                                                       @RequestParam(name = "search_value", required = false) String warehouse_name,
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

            generalResponse.setData(bookingService.getBookingRequestByStatus(status, warehouse_name, role_login_user.get(), page, size));
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
     * Retrieve all data booking warehouse by its ID.
     *
     * @param username
     * @param session
     * @param id
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getDetailBookingWarehouse(@RequestHeader(name = "username", required = false) String username,
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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            generalResponse = bookingService.getDetailBookingRequestById(id, role_login_user.get());

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
     * This controller used for get count notification booking warehouse based on user login.
     * There is 5 role login :
     *  - Super Admin(1)
     *  - Admin(2)
     *  - Seller(3)
     *  - Buyer(4)
     *  - SubSeller(5)
     *
     * @param username
     * @param session
     * @return {@link GeneralResponse}
     */
    @Deprecated
    @GetMapping(value = RestConstant.GET_NOTIFICATION, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getNotificationBooking(@RequestHeader(name = "username", required = false) String username,
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

            /*username = jwt.getUsername();

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());*/

            generalResponse.setCode(Constant.Code.SUCCESS.getStatusCode());
            /*generalResponse.setData(bookingService.getNotificationBooking(role_login_user.get()));*/

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
     * This controller used for change status booking warehouse. Admin and Seller can approve
     * or Reject the booking request and can type the reason why those rejected.
     * There is 8 status booking :
     *  - OPEN(1)
     *  - SELLER_REVIEW(2)
     *  - SELLER_APPROVED(3)
     *  - ADMIN_REJECTED(4)
     *  - QUOTATION_REQUESTED(5)
     *  - ADMIN_APPROVED(6)
     *  - SELLER_REJECTED(7)
     *  - ADMIN_APPROVED_REVIEW(8)
     *
     * @param username
     * @param session
     * @param action
     * @param orderRequestModel
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.CHANGE_STATUS)
    ResponseEntity<GeneralResponse> updateStatusBooking(@RequestHeader(name = "username", required = false) String username,
                                                        @RequestHeader(name = "session") String session,
                                                        @RequestParam(name = "action") int action,
                                                        @RequestBody OrderRequestModel orderRequestModel,
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

            generalResponse.setCode(bookingService.updateStatus(orderRequestModel, role_login_user.get(), action));
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
     * This controller used for download quotation. Only buyer can access this controller and
     * only booking order that has status QUOTATION_REQUESTED(5) can be downloaded.
     *
     * @param id
     * @return {@link Resource}
     * @throws Exception
     */
    @GetMapping(value = RestConstant.DOWNLOAD+RestConstant.QUOTATION)
    public ResponseEntity<Resource> downloadQuotation(@RequestParam("id") int id) throws Exception {

        String file_name = generatePDFFile.generateQuotation(id);

        File file = new File("/var/www/core/"+file_name);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file_name);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                file.delete();
                util.deleteImage();
            }
        }).start();

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
