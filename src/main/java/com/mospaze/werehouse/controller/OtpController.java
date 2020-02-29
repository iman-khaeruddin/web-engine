package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.entity.ModelHasRole;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.JWT;
import com.mospaze.werehouse.model.ValidateOTPRequest;
import com.mospaze.werehouse.repository.ModelHasRoleDao;
import com.mospaze.werehouse.repository.UsersDao;
import com.mospaze.werehouse.service.LoginService;
import com.mospaze.werehouse.service.OtpService;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_OTP)
public class OtpController {

    private final Logger logger = LoggerFactory.getLogger(OtpController.class);

    @Autowired
    private Util util;

    @Autowired
    private OtpService otpService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ModelHasRoleDao modelHasRoleDao;

    @Autowired
    private UsersDao usersDao;


    /**
     * This controller used for resend OTP,
     * consumed by MIT application (mospaze internal tools - for internal purpose only).
     * 
     * @param username
     * @param session
     * @param phone
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.RESEND_OTP)
    @ResponseBody
    ResponseEntity<GeneralResponse> resendOTP(@RequestHeader(name = "username", required = false) String username,
                                              @RequestHeader(name = "session") String session,
                                              @RequestParam("phone") String phone) {

        logger.info("RequestHeader [username] : {}", username);
        logger.info("RequestHeader [session] : {}", session);
        logger.info("RequestParam [phone] : {}", phone);

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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());
            if(role_login_user.get().getRole_id() != Constant.Role.ADMIN.getRole() &&
                role_login_user.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(otpService.resendOtp(phone));
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

    /**
     * This controller used for see all OTP list,
     * consumed by MIT application (mospaze internal tools - for internal purpose only).
     * 
     * @param username
     * @param session
     * @param page
     * @param size
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse> getOtp(@RequestHeader(name = "username", required = false) String username,
                                                  @RequestHeader(name = "session") String session,
                                                  @RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "size") Integer size,
                                                  HttpServletRequest httpServletRequest) {

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());
        logger.info("RequestParam [username] : {}", username);
        logger.info("RequestParam [session] : {}", session);
        logger.info("RequestParam [page] : {}", page);
        logger.info("RequestParam [size] : {}", size);

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

            Optional<ModelHasRole> role_login_user = modelHasRoleDao.findById(usersDao.getUserByPhone(username).get().getId());
            if(role_login_user.get().getRole_id() != Constant.Role.ADMIN.getRole() &&
                    role_login_user.get().getRole_id() != Constant.Role.SUPER_ADMIN.getRole()){
                generalResponse.setCode(Constant.Code.ROLE_NOT_ALLOWED.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setData(otpService.getListOtp(page, size));
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

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for Force change password ONLY.
     *
     * After using this controller you should
     * validate using {@link OtpController#validateOTP(ValidateOTPRequest)}.
     *
     * @param phone
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.REQUEST_OTP)
    ResponseEntity<GeneralResponse> requestOTP(@RequestParam("phone") String phone) {

        logger.info("RequestParam [phone] : {}", phone);

        GeneralResponse generalResponse = new GeneralResponse();

        try {

            if (!Constant.PHONE_VALIDATION_REGEX.matcher(phone).matches()) {
                generalResponse.setCode(Constant.Code.PHONE_NUMBER_INVALID.getStatusCode());
                generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
                generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

                logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

                return new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }

            generalResponse.setCode(otpService.requestOTPForceChangePassword(phone));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    /**
     * This controller used for validate OTP.
     *
     * Force change password mechanism (for old user) using this controller.
     *
     * You should request OTP {@link OtpController#requestOTP(String)} before using this controller.
     *
     * @param validateOTPRequest
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.VALIDATE_OTP)
    @ResponseBody
    ResponseEntity<GeneralResponse> validateOTP(@Valid @RequestBody ValidateOTPRequest validateOTPRequest) {

        logger.info("REQUEST : {}", validateOTPRequest.toString());

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse.setCode(otpService.validateOtp(validateOTPRequest));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("en_message", fieldName + " " + errorMessage);
            errors.put("id_message", fieldName + " " + errorMessage);
            errors.put("code", Constant.Code.GENERAL_ERROR.getStatusCode());
        });
        return errors;
    }
}
