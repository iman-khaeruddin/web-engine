package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.model.ChangePasswordRequest;
import com.mospaze.werehouse.model.ForgotPasswordRequest;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.service.ForgotPasswordService;
import com.mospaze.werehouse.service.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_FORGOT_PASSWORD)
class ForgotPasswordController {

    private final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private UserManagementService userManagementService;

    /**
     * This controller used for validate email. This step required in forgot password mechanism.
     * It must be ensured that the email is registered in the system. If email registered,
     * the system will send email that contain link for change password.
     *
     * @param forgotPasswordRequest
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.VERIFY_EMAIL)
    ResponseEntity<GeneralResponse> validateEmail(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){

        logger.info("REQUEST : " + forgotPasswordRequest.toString());

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse = forgotPasswordService.validateEmail(forgotPasswordRequest);
        }catch (Exception e){
            logger.error("see stack trace : {}", e.getMessage());
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.ACCEPTED);

    }

    /**
     * This controller used for validate token.
     * After user receive the email that contain link for change password,
     * the link that contain token must be verify using this controller.
     *
     * @param token
     * @param username
     * @param httpServletRequest
     * @return {@link GeneralResponse}
     */
    @GetMapping(value = RestConstant.VERIFY_TOKEN)
    ResponseEntity<GeneralResponse> validateToken(@RequestParam(name = "token") String token,
                                                  @RequestParam(name = "username") String username,
                                                  HttpServletRequest httpServletRequest){

        logger.info("REQUEST URL : {}", httpServletRequest.getRequestURL());
        logger.info("RequestParam [token] : {}", token);
        logger.info("RequestParam [username] : {}", username);

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse = forgotPasswordService.validateToken(token, username);
        }catch (Exception e){
            logger.error("see stack trace : {}", e.getMessage());
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("No entity found for query; nested exception is javax.persistence.NoResultException: No entity found for query"))
                generalResponse.setCode(Constant.Code.TOKEN_INVALID.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

        return new ResponseEntity<>(generalResponse, HttpStatus.OK);

    }

    /**
     * After step {@link #validateToken(String, String, HttpServletRequest)} got response success,
     * the next step is using this controller. User can put new password and
     * API will update to system.
     *
     * @param token
     * @param username
     * @param changePasswordRequest
     * @param bindingResult
     * @return {@link GeneralResponse}
     */
    @PutMapping(value = RestConstant.CHANGE_PASSWORD)
    ResponseEntity<GeneralResponse> forgotPassword(@RequestHeader(name = "token") String token,
                                                   @RequestHeader(name = "username") String username,
                                                   @Validated @RequestBody ChangePasswordRequest changePasswordRequest,
                                                   BindingResult bindingResult){

        logger.info("REQUEST : " + changePasswordRequest.toString());
        logger.info("RequestParam [token] : {}", token);
        logger.info("RequestParam [username] : {}", username);

        GeneralResponse generalResponse = new GeneralResponse();

        if (bindingResult.hasErrors()) {
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());
            generalResponse.setEn_message(bindingResult.getAllErrors().toString());

            logger.info("RESPONSE : " + generalResponse.toString() + System.lineSeparator());

            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        }

        try {
            generalResponse = forgotPasswordService.validateToken(token, username);

            if(generalResponse.getCode().equals(Constant.Code.SUCCESS.getStatusCode()))
                generalResponse = userManagementService.forgotPassword(changePasswordRequest, username, token);

        }catch (Exception e){
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.toString());
            e.printStackTrace();
            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.getMessage().contains("No entity found for query; nested exception is javax.persistence.NoResultException: No entity found for query"))
                generalResponse.setCode(Constant.Code.TOKEN_INVALID.getStatusCode());

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
