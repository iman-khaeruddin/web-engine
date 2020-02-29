package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.NotificationService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(RestConstant.INDEX_NOTIFICATION)
public class NotoficationController {

    private Logger logger = LoggerFactory.getLogger(NotoficationController.class);

    @Autowired
    private Util util;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private NotificationService notificationService;

    /**
     * This controller used to retrieve all notifications including warehouse and booking status.
     *
     * @param session
     * @param type
     * @param page
     * @param size
     * @param httpServletRequest
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getNotifications(@RequestHeader(name = "session") String session,
                                                            @RequestParam(name = "type") Integer type,
                                                            @RequestParam(name = "page") Integer page,
                                                            @RequestParam(name = "size") Integer size,
                                                            HttpServletRequest httpServletRequest) {

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());
        logger.info("RequestParam [page] : {}", page);
        logger.info("RequestParam [size] : {}", size);
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

            String username = jwt.getUsername();
            int user_id = 0;

            Optional<ModelHasRole> user_role_login = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());

            if(user_role_login.get().getRole_id() != Constant.Role.ADMIN.getRole() && user_role_login.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole()){
                user_id = user_role_login.get().getModel_id();
            }
            if(user_role_login.get().getRole_id() == Constant.Role.SUB_SELLER.getRole()){
                user_id = user_role_login.get().getSuper_user();
            }

            generalResponse.setData(notificationService.getNotification(user_id, type, page, size));
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

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for update notification status from unread to read or vice versa.
     *
     * @param session
     * @param is_read
     * @param id
     * @param httpServletRequest
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.CHANGE_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> changeStatusNotification(@RequestHeader(name = "session") String session,
                                                            @RequestParam(name = "is_read") Integer is_read,
                                                            @RequestParam(name = "id") Integer id,
                                                            HttpServletRequest httpServletRequest) {

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

            if(is_read == 1) {
                generalResponse.setCode(notificationService.changeStatusRead(id, jwt));
            } else if(is_read == 0) {
                generalResponse.setCode(notificationService.changeStatusUnRead(id, jwt));
            } else {
                generalResponse.setCode(Constant.Code.ACTION_INVALID.getStatusCode());
            }

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

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

}
