package com.mospaze.werehouse.controller;

import com.mospaze.werehouse.common.Constant;
import com.mospaze.werehouse.common.RestConstant;
import com.mospaze.werehouse.common.Util;
import com.mospaze.werehouse.model.GeneralResponse;
import com.mospaze.werehouse.model.RegisterRequest;
import com.mospaze.werehouse.service.RegisterUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = RestConstant.INDEX_REGISTER)
class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterUserService registerUserService;

    @Autowired
    private Util util;

    /**
     * This controller used for register user (seller/buyer). This controller required OTP.
     * @see #requestOTP(RegisterRequest)
     *
     * @param registerRequest
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.INDEX_USER_MANAGEMENT)
    HttpEntity<GeneralResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse.setCode(registerUserService.registerUser(registerRequest));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.toString().contains("UC_Users"))
                generalResponse.setCode(Constant.Code.PHONE_DUPLICATE.getStatusCode());
            if (e.toString().contains("users_username_unique"))
                generalResponse.setCode(Constant.Code.EMAIL_DUPLICATE.getStatusCode());
            if (e.toString().contains("username_UNIQUE"))
                generalResponse.setCode(Constant.Code.EMAIL_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

        return new ResponseEntity<>(generalResponse, generalResponse.getCode().equals(Constant.Code.SUCCESS.getStatusCode()) ? HttpStatus.CREATED : HttpStatus.OK);
    }

    /**
     * This controller used for request OTP. OTP required in register user (seller/buyer).
     * @see #registerUser(RegisterRequest)
     *
     * @param registerRequest
     * @return {@link GeneralResponse}
     */
    @PostMapping(value = RestConstant.REQUEST_OTP)
    HttpEntity<GeneralResponse> requestOTP(@Valid @RequestBody RegisterRequest registerRequest) {

        GeneralResponse generalResponse = new GeneralResponse();

        try {
            generalResponse.setCode(registerUserService.requestOTPRegister(registerRequest));
            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));

        } catch (Exception e) {
            logger.error("see stack trace : {}", e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();

            generalResponse.setCode(Constant.Code.GENERAL_ERROR.getStatusCode());

            if (e.toString().contains("UC_Users"))
                generalResponse.setCode(Constant.Code.PHONE_DUPLICATE.getStatusCode());
            if (e.toString().contains("users_username_unique"))
                generalResponse.setCode(Constant.Code.EMAIL_DUPLICATE.getStatusCode());

            generalResponse.setEn_message(Constant.getEnMessage(generalResponse.getCode()));
            generalResponse.setId_message(Constant.getInMessage(generalResponse.getCode()));
        }

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
